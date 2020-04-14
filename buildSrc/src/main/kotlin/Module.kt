package com.tokopedia.plugin

import org.gradle.api.Project

data class Module(val project: Project,
                  val type: ModuleType,
                  val key:String) {

}

enum class ModuleType(val typeInt: Int) {
    APP(0),
    LIBRARY(1),
    DF(2),
    OTHER(3)
}