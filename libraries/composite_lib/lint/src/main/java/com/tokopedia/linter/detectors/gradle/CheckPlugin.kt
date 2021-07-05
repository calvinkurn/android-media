package com.tokopedia.linter.detectors.gradle

fun checkPlugin(filePath:String,plugin:String,project:String) {
    checkHanselPlugin(filePath,plugin, project)
}