package com.tokopedia.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class ScanReleaseDateTask : DefaultTask() {
    var latestReleaseDate = ""

    //@InputFile
    val file = File ("tools/version/release_date.txt")

    @TaskAction
    fun run() {
        file.forEachLine {
            if (it.isNotEmpty()) {
                latestReleaseDate = it
            }
        }
        println(latestReleaseDate)
    }
}