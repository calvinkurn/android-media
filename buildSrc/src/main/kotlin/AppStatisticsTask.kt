package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class AppStatisticsTask : DefaultTask() {

    @OutputFile
    var reportFile: File = File("finalReport.csv")

    var containCoreMap: MutableMap<String, Boolean> = hashMapOf()
    val flattenedAllSubProjects = mutableListOf<Module>()
    val dependenciesHashSetTemp = HashSet<Pair<String, String>>()

    companion object {
        const val FILE_SEPARATOR = ","
        const val MODULE = "Module"
        const val MODULE_PATH = "Path"
        const val TYPE = "Type"
        const val CONTAIN_CORE = "Contain Core"
        const val DF = "DF"
        const val DF_ENABLED = "DF enabled"
        const val JAVA_COUNT = "Java Count"
        const val JAVA_SIZE = "Java Size"
        const val KOTLIN_COUNT = "Kotlin Count"
        const val KOTLIN_SIZE = "Kotlin Size"
        const val KOTLIN_COUNT_PERCENT = "Kotlin Count%"
        const val KOTLIN_SIZE_PERCENT = "Kotlin Size%"
        const val TOTAL_SIZE = "Total Size"
        val metricList = listOf(MODULE, MODULE_PATH, TYPE, CONTAIN_CORE, DF, DF_ENABLED,
            JAVA_COUNT, JAVA_SIZE, KOTLIN_COUNT, KOTLIN_SIZE,
            KOTLIN_COUNT_PERCENT, KOTLIN_SIZE_PERCENT, TOTAL_SIZE)
    }

    @TaskAction
    fun run() {
        reportFile.overwrite()

        val queue = mutableListOf<Project>(project)
        //BFS to get all projects
        while (queue.isNotEmpty()) {
            val projectItem = queue.removeAt(0)
            queue.addAll(projectItem.childProjects.values)

            if (projectItem == project) {
                // this is to ignore the root project
                continue
            }

            val module = Module(projectItem, projectItem.getModuleType(), projectItem.name.trimSymbol())
            flattenedAllSubProjects.add(module)

            when (module.type) {
                ModuleType.APP, ModuleType.LIBRARY -> {
                    val projectName = projectItem.name
                    println(projectItem.absoluteProjectPath(projectItem.path))

                    projectItem.configurations.all { conf ->
                        conf.dependencies.forEach {
                            // this is to make hashCode of combination of libraries and those version
                            // If there are version changes in the libraries, the module will become a candidate for version increase.
                            val artifactName = it.name
                            // create list tree
                            dependenciesHashSetTemp.add(projectName to artifactName)

                            if (artifactName == "tkpdcore" || artifactName == "core_legacy") {
                                containCoreMap[module.key] = true
                            }
                        }
                    }
                }
                ModuleType.DF -> {

                }
            }


        }
        printFinalReport()
    }

    fun printFinalReport() {
        reportFile.appendText(metricList.joinToString(FILE_SEPARATOR) + "\n")
        flattenedAllSubProjects.filter { it.type == ModuleType.APP || it.type == ModuleType.LIBRARY }.forEach {
            val projectName = it.project.name
            for ((i, metric) in metricList.withIndex()) {
                if (i != 0) {
                    reportFile.appendText(FILE_SEPARATOR)
                }
                reportFile.appendText(
                    when (metric) {
                        MODULE -> it.project.friendlyName()
                        MODULE_PATH -> it.project.absoluteProjectPath(it.project.path)
                        TYPE -> getTypeString(it.type)
                        CONTAIN_CORE -> containCoreString(it.key)
                        else -> ""
                    }
                )
            }
            reportFile.appendText("\n")
//            reportFile.appendText(
//                it.project.friendlyName() +
//                    FILE_SEPARATOR +
//                    getTypeString(it.type) +
//                    FILE_SEPARATOR +
//                    containCoreString(projectName)
//                    +
//                    "#" + if (propList.any { prop -> keyTrimmedUnder.contains(prop) }) {
//                    "OK"
//                } else {
//                    " "
//                } +
//                    "#" + it.value.javaCount +
//                    "#" + it.value.javaSize +
//                    "#" + it.value.kotlinCount +
//                    "#" + it.value.kotlinSize +
//                    "#" + round((it.value.kotlinCount.toDouble() / (it.value.javaCount + it.value.kotlinCount) * 10000)) / 100 +
//                    "#" + round((it.value.kotlinSize.toDouble() / (it.value.javaSize + it.value.kotlinSize) * 10000)) / 100 +
//                    "#" + round((it.value.kotlinSize + it.value.javaSize).toDouble() * 100 / 1024) / 100 +
//                    + "\n"
//            )
        }
    }

    fun String.trimSymbol(): String {
        return replace("_", "").replace("-", "")
    }

    fun containCoreString(projectName: String): String {
        return if (containCoreMap[projectName] == true) {
            "X"
        } else {
            " "
        }
    }

    fun Project.friendlyName(): String {
        val projectName = project.name
        var projectNameFriendlyString = if (projectName.startsWith(":")) {
            projectName.replaceFirst(":", "")
        } else {
            projectName
        }
        return projectNameFriendlyString.replace(":", "")
    }

    fun File.overwrite() {
        if (exists()) {
            delete()
        }
        createNewFile()
    }

    fun getTypeString(moduleType: ModuleType): String {
        return when (moduleType) {
            ModuleType.LIBRARY -> "library"
            ModuleType.APP -> "application"
            ModuleType.DF -> "dynamic-feature"
            ModuleType.OTHER -> "other"
        }
    }

    fun Project.getModuleType(): ModuleType {
        return if (plugins.hasPlugin("com.android.library")) {
            ModuleType.LIBRARY
        } else if (plugins.hasPlugin("com.android.application")) {
            ModuleType.APP
        } else if (plugins.hasPlugin("com.android.dynamic-feature")) {
            ModuleType.DF
        } else {
            ModuleType.OTHER
        }
    }
}