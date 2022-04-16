package com.tokopedia.media.picker.helper.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object VideoGenerator {

    private val mockVideoFileNameList = listOf(
        "test_mp_videos.mp4"
    )

    fun generateVideos(context: Context): List<File> {
        return mockVideoFileNameList.map {
            streamToFile(context, it)
        }
    }

    fun getFiles(context: Context): List<File> {
        return mockVideoFileNameList.map {
            File(context.externalCacheDir, it)
        }
    }

    private fun streamToFile(context: Context, fileName: String): File {
        val inputStream = context.resources.assets.open(fileName)

        // save similar file name into cache dir
        val outputFile = File(context.externalCacheDir, fileName)

        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)

            outputStream.use { output ->
                val buffer = ByteArray(2 * 1024) //  buffer size of video

                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }

                output.flush()
            }
        }

        return outputFile
    }

}