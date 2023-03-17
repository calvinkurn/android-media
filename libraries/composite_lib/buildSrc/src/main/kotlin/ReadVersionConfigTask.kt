package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ReadVersionConfigTask : DefaultTask() {

    @Internal
    val versionConfigMap = mutableMapOf<String, Int>()

    @Internal
    var versionSuffix: String = ""

    companion object {
        const val CONFIG_VERSION_FILE_PATH = "tools/version/config_version.txt"
    }

    @TaskAction
    fun run() {
        val file = File(CONFIG_VERSION_FILE_PATH)
        file.forEachLine { line ->
            if (line.isNotEmpty() && !line.startsWith("//")) {
                val splits = line.split("=")
                if (splits[0] == "Suffix") {
                    versionSuffix = splits[1]
                } else {
                    versionConfigMap[splits[0]] = splits[1].toInt()
                }
            }
        }
    }
}
