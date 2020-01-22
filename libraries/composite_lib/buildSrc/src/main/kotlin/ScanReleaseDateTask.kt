package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

open class ScanReleaseDateTask : DefaultTask() {
    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    var moduleLatestLogMap = hashMapOf<String, Date>()

    val file = File("tools/version/module_latest_log.txt")

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @TaskAction
    fun run() {
        file.forEachLine {
            if (it.isNotEmpty() && !it.startsWith("//")) {
                val lineSplit = it.split("#")
                if (lineSplit.size == 2) {
                    val moduleName = lineSplit[0]
                    val date = lineSplit[1]
                    moduleLatestLogMap.put(moduleName, dateFormatter.parse(date))
                }
            }
        }
    }
}