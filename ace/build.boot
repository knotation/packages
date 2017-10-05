(set-env!
  :resource-paths #{"resources"}
  :dependencies '[[cljsjs/boot-cljsjs "0.7.1" :scope "test"]])

(def +lib-version+ "1.2.8")
(def +version+ (str +lib-version+ "-0"))

(require '[cljsjs.boot-cljsjs.packaging :refer :all])

(task-options!
  pom {:project     'cljsjs/ace
       :version     +version+
       :description "The Ajax.org Cloud9 Editor"
       :url         "https://ace.c9.io/"
       :license     {"BSD" "https://opensource.org/licenses/BSD-3-Clause"}
       :scm         {:url "https://github.com/cljsjs/packages"}})

(deftask package []
  (comp
   (download :url "https://github.com/ajaxorg/ace-builds/archive/812e2c56aed246931a667f16c28b096e34597016.zip"
             :name (format "ace-%s.zip" +lib-version+)
             :checksum "4319b565336d9d2293ec96a0941dbb08"
             :unzip true)
   (sift :move {#"ace-builds-(.+)/src/(.*).js" "cljsjs/development/$2.inc.js"
                #"ace-builds-(.+)/src-min/(.*).js" "cljsjs/production/$2.min.inc.js"})
   (sift :include #{#"^cljsjs"})
   (deps-cljs :name "cljsjs.ace")
   (pom)
   (jar)))
