package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.lang.Math.round

open class AppStatisticsTask : DefaultTask() {

    @OutputFile
    var reportFile: File = File("finalReport.csv")

    var containCoreMap: MutableMap<String, Boolean> = hashMapOf()
    val flattenedAllSubProjects = mutableListOf<Module>()
    val dfList = mutableSetOf<String>()
    val dfEnableList = mutableSetOf<String>()
    var ownerListMap: MutableMap<String, String> = hashMapOf()
    var javaFileCountMap: MutableMap<String, Int> = hashMapOf()
    var kotlinFileCountMap: MutableMap<String, Int> = hashMapOf()
    var javaFileSizeMap: MutableMap<String, Long> = hashMapOf()
    var kotlinFileSizeMap: MutableMap<String, Long> = hashMapOf()

    var drawableFileCountMap: MutableMap<String, Int> = hashMapOf()
    var otherResFileCountMap: MutableMap<String, Int> = hashMapOf()
    var drawableFileSizeMap: MutableMap<String, Long> = hashMapOf()
    var otherResFileSizeMap: MutableMap<String, Long> = hashMapOf()

    var unitTestCountMap: MutableMap<String, Long> = hashMapOf()
    var androidTestCountMap: MutableMap<String, Long> = hashMapOf()

    companion object {
        const val FILE_SEPARATOR = ","
        const val MODULE = "Module"
        const val MODULE_PATH = "Path"
        const val TYPE = "Type"
        const val CONTAIN_CORE = "Contain Core"
        const val DF = "DF"
        const val DF_ENABLED = "DF enabled"
        const val OWNER = "Owner"
        const val JAVA_COUNT = "Java Count"
        const val JAVA_SIZE = "Java Size"
        const val KOTLIN_COUNT = "Kotlin Count"
        const val KOTLIN_SIZE = "Kotlin Size"
        const val KOTLIN_COUNT_PERCENT = "Kotlin Count%"
        const val KOTLIN_SIZE_PERCENT = "Kotlin Size%"
        const val TOTAL_CODE_SIZE = "Total Code Size"
        const val DRAWABLE_SIZE = "Drawable Size"
        const val OTHER_RES_SIZE = "Other Res Size"
        const val TOTAL_SIZE = "Total Size"
        const val HAS_UNIT_TEST = "Has Unit Test"
        const val HAS_ANDROID_TEST = "Has Android Test"
        val metricList = listOf(MODULE, MODULE_PATH, TYPE, OWNER, CONTAIN_CORE, DF, DF_ENABLED,
            JAVA_COUNT, KOTLIN_COUNT, KOTLIN_COUNT_PERCENT,
            JAVA_SIZE, KOTLIN_SIZE, KOTLIN_SIZE_PERCENT, TOTAL_CODE_SIZE,
            DRAWABLE_SIZE, OTHER_RES_SIZE, TOTAL_SIZE, HAS_UNIT_TEST, HAS_ANDROID_TEST)
    }

    @TaskAction
    fun run() {
        val startTime = System.currentTimeMillis()
        reportFile.overwrite()

        getOwnerList()

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
                ModuleType.APP -> {
                    projectItem.configurations.all { conf ->
                        conf.dependencies.forEach {
                            // this is to make hashCode of combination of libraries and those version
                            // If there are version changes in the libraries, the module will become a candidate for version increase.
                            val artifactName = it.name
                            if (artifactName == "tkpdcore" || artifactName == "core_legacy") {
                                containCoreMap[module.key] = true
                            }
                            dfEnableList.add(artifactName)
                        }
                    }
                    val moduleDir = projectItem.projectDir
                    moduleDir.calcFileStats(module.key)
                }
                ModuleType.LIBRARY -> {
                    projectItem.configurations.all { conf ->
                        conf.dependencies.forEach {
                            // this is to make hashCode of combination of libraries and those version
                            // If there are version changes in the libraries, the module will become a candidate for version increase.
                            val artifactName = it.name
                            if (artifactName == "tkpdcore" || artifactName == "core_legacy") {
                                containCoreMap[module.key] = true
                            }
                            if (dfEnableList.contains(artifactName)) {
                                dfEnableList.remove(artifactName)
                            }
                        }
                    }
                    val moduleDir = projectItem.projectDir
                    moduleDir.calcFileStats(module.key)
                }
                ModuleType.DF -> {
                    projectItem.configurations.all { conf ->
                        conf.dependencies.forEach {
                            // this is to make hashCode of combination of libraries and those version
                            // If there are version changes in the libraries, the module will become a candidate for version increase.
                            val artifactName = it.name
                            dfList.add(artifactName)
                        }
                    }
                }
            }
        }
        printFinalReport()

        val endTime = System.currentTimeMillis()
        print("Generate Report in " + (endTime - startTime) + "ms")
    }

    fun getOwnerList() {
        val codeowner = File("CODEOWNERS")
        codeowner.forEachLine {
            if (it.isNotEmpty()) {
                var (modulePath, owner) = it.split("@")
                if (modulePath.isNotEmpty()) {
                    modulePath = modulePath.trim()
                    if (modulePath.endsWith("/")) {
                        modulePath = modulePath.substring(0, modulePath.length - 1)
                    }
                    val moduleNamePathList = modulePath.split("/")
                    val moduleName = moduleNamePathList.last()
                    owner = owner.trim()
                    ownerListMap.put(moduleName, owner)
                }
            }
        }
    }

    fun printFinalReport() {
        reportFile.appendText(metricList.joinToString(FILE_SEPARATOR) + "\n")
        flattenedAllSubProjects.filter { it.type == ModuleType.APP || it.type == ModuleType.LIBRARY }.forEach {
            val projectName = it.project.name
            for ((i, metric) in metricList.withIndex()) {
                if (i != 0) {
                    reportFile.appendText(FILE_SEPARATOR)
                }
                val path = it.project.absoluteProjectPath(it.project.path)
                val javaCount = javaFileCountMap[it.key] ?: 0
                val kotlinCount = kotlinFileCountMap[it.key] ?: 0
                val javaSize = javaFileSizeMap[it.key] ?: 0
                val kotlinSize = kotlinFileSizeMap[it.key] ?: 0
                val drawableSize = drawableFileSizeMap[it.key] ?: 0
                val otherResSize = otherResFileSizeMap[it.key] ?: 0
                reportFile.appendText(
                    when (metric) {
                        MODULE -> it.project.friendlyName()
                        MODULE_PATH -> path
                        TYPE -> getTypeString(it.type, path)
                        CONTAIN_CORE -> containCoreString(it.key)
                        DF -> isDF(projectName)
                        DF_ENABLED -> isDFenabled(projectName)
                        OWNER -> getOwner(projectName)
                        JAVA_COUNT -> javaCount.toString()
                        KOTLIN_COUNT -> kotlinCount.toString()
                        JAVA_SIZE -> javaSize.toString()
                        KOTLIN_SIZE -> kotlinSize.toString()
                        KOTLIN_COUNT_PERCENT -> percent(kotlinCount.toLong(), kotlinCount.toLong() + javaCount).toString() + "%"
                        KOTLIN_SIZE_PERCENT -> (percent(kotlinSize, kotlinSize + javaSize).toString() + "%")
                        TOTAL_CODE_SIZE -> (javaSize + kotlinSize).toString()
                        DRAWABLE_SIZE -> drawableSize.toString()
                        OTHER_RES_SIZE -> otherResSize.toString()
                        TOTAL_SIZE -> (javaSize + kotlinSize + drawableSize + otherResSize).toString()
                        HAS_UNIT_TEST -> (unitTestCountMap[it.key] ?: 0).toString()
                        HAS_ANDROID_TEST -> (androidTestCountMap[it.key] ?: 0).toString()
                        else -> ""
                    }
                )
            }
            reportFile.appendText("\n")
        }
    }

    fun percent(value: Long, denominator: Long): Double =
        (round((value.toDouble() / denominator.toDouble() * 10000)).toDouble() / 100)

    val CODE_TYPE = "CODE"
    val ANDROID_TEST_TYPE = "ANDROID_TEST"
    val UNIT_TEST_TYPE = "UNIT_TEST"
    val GRADLE_TYPE = "GRADLE"
    val DRAWABLE_TYPE = "DRAWABLE"
    val OTHER_RESOURCE = "OTHER_RES"

    fun File.calcFileStats(key: String) {
        listFiles()?.forEach {
            if (it.isDirectory && it.name != "build" &&
                it.name != "libs" &&
                it.name != "gradle" &&
                it.name != "buildsrc" &&
                !it.name.contains("no_op")
            ) {
                it.calcFileStats(key)
            } else {
                val type = if (it.extension == ".gradle") {
                    GRADLE_TYPE
                } else if (it.absolutePath.contains("src/androidTest")) {
                    ANDROID_TEST_TYPE
                } else if (it.absolutePath.contains("src/test")) {
                    UNIT_TEST_TYPE
                } else if (it.absolutePath.contains("src/main/res/drawable")) {
                    DRAWABLE_TYPE
                } else if (it.absolutePath.contains("src/main/res")) {
                    OTHER_RESOURCE
                } else {
                    CODE_TYPE
                }
                when (type) {
                    CODE_TYPE -> {
                        if (it.extension == "java") {
                            val count = javaFileCountMap.getOrDefault(key, 0)
                            javaFileCountMap[key] = count + 1
                            val size = javaFileSizeMap.getOrDefault(key, 0)
                            javaFileSizeMap[key] = (size + it.length())
                        } else if (it.extension == "kt") {
                            val count = kotlinFileCountMap.getOrDefault(key, 0)
                            kotlinFileCountMap[key] = count + 1
                            val size = kotlinFileSizeMap.getOrDefault(key, 0)
                            kotlinFileSizeMap[key] = (size + it.length())
                        }
                    }
                    ANDROID_TEST_TYPE -> {
                        if (!it.name.contains("Example") && (it.extension == "java" || it.extension == "kt")) {
                            val count = androidTestCountMap.getOrDefault(key, 0)
                            androidTestCountMap[key] = count + 1
                        }
                    }
                    UNIT_TEST_TYPE -> {
                        if (!it.name.contains("Example") && (it.extension == "java" || it.extension == "kt")) {
                            val count = unitTestCountMap.getOrDefault(key, 0)
                            unitTestCountMap[key] = count + 1
                        }
                    }
                    DRAWABLE_TYPE -> {
                        val count = drawableFileCountMap.getOrDefault(key, 0)
                        drawableFileCountMap[key] = count + 1
                        val size = drawableFileSizeMap.getOrDefault(key, 0)
                        drawableFileSizeMap[key] = (size + it.length())
                    }
                    OTHER_RESOURCE -> {
                        val count = otherResFileCountMap.getOrDefault(key, 0)
                        otherResFileCountMap[key] = count + 1
                        val size = otherResFileSizeMap.getOrDefault(key, 0)
                        otherResFileSizeMap[key] = (size + it.length())
                    }
                }
            }
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

    fun isDF(projectName: String): String {
        return if (dfList.contains(projectName)) {
            "OK"
        } else {
            " "
        }
    }

    fun isDFenabled(projectName: String): String {
        return if (dfEnableList.contains(projectName)) {
            "OK"
        } else {
            " "
        }
    }

    fun getOwner(projectName: String): String {
        return ownerListMap.getOrDefault(projectName, " ")
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

    fun getTypeString(moduleType: ModuleType, path: String): String {
        return when (moduleType) {
            ModuleType.LIBRARY -> if (path.contains("libraries")) {
                "library"
            } else {
                "feature"
            }
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