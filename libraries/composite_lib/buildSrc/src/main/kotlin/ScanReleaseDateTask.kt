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
    var latestReleaseDate: Date = Date()
    val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    //@InputFile
    val file = File("tools/version/release_date.txt")

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @TaskAction
    fun run() {
        var latestReleaseDateString = ""
        file.forEachLine {
            if (it.isNotEmpty()) {
                latestReleaseDateString = it
            }
        }
        latestReleaseDate = dateFormatter.parse(latestReleaseDateString)
    }
}