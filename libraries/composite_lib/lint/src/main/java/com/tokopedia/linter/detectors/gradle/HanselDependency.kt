package com.tokopedia.linter.detectors.gradle

import com.android.ide.common.blame.SourcePosition
import com.android.tools.lint.detector.api.*
import com.tokopedia.linter.LinterConstants.GradleConstructs.BUILD_SCRIPT
import com.tokopedia.linter.LinterConstants.GradleConstructs.DEPENDENCIES
import com.tokopedia.linter.LinterConstants.GradleConstructs.REPOSITORIES

class HanselDependency {
    var isPluginAdded = false;
    var isRepositoryAdded = false;
    var isClassPathAdded = false;

    fun isHanselAdded() = isPluginAdded && isRepositoryAdded && isClassPathAdded


}


val HANSEL_REQUIRED = Issue.create(
        "Hansel PLUGIN",  //$NON-NLS-1$
        "Hansel PLUGIN SHOULD BE INCLUDED",
        "Hansel IS IMPORTANT LIBRARY PLEASE HELP TO INCLUDE",
        Category.CORRECTNESS,
        1,
        Severity.ERROR,
        GradleDetector.IMPLEMENTATION
)

const val HANSEL_PLUGIN = "io.hansel.preprocessor.module"
const val HANSEL_REPOSITORY = "maven { url 'https://maven-hansel.tokopedia.com/maven' }"
const val HANSEL_CLASSPATH = "classpath rootProject.ext.miscDependencies.hansel"

var mapGraleFileTOHansel = mutableMapOf<String, HanselDependency?>()

fun addGradleFileToMap(filePath: String) = {
    mapGraleFileTOHansel[filePath] = null
}

fun checkHanselPlugin(filePath: String, plugin: String, project: String) {
    if (plugin == HANSEL_PLUGIN) {
        getHanselObject(filePath).isPluginAdded = true;
    }
}

fun checkHanselRepository(filePath: String, property: String, parent: String, value: String) {
    if (parent == BUILD_SCRIPT) {
        if (property == REPOSITORIES) {
            if (value.contains(HANSEL_REPOSITORY)) {
                getHanselObject(filePath).isRepositoryAdded = true
            }
        }
    }

}

fun checkHanselClassPath(filePath: String, property: String, parent: String, value: String) {
    if (parent == BUILD_SCRIPT) {
        if (property == DEPENDENCIES) {
            if (value.contains(HANSEL_CLASSPATH)) {
                getHanselObject(filePath).isClassPathAdded = true
            }
        }
    }
}


fun getHanselObject(filePath: String) = mapGraleFileTOHansel[filePath] ?: HanselDependency().apply {
    mapGraleFileTOHansel[filePath] = this
}

fun checkHanselPresent(filePath: String, context: Context) {
    if (!getHanselObject(filePath).isHanselAdded()) {
        context.report(
                HANSEL_REQUIRED, Location.create(context.file, SourcePosition(3,0,0)), "HANSEL PLUGIN SHOULD BE INCLUDED"
        )
    }
}