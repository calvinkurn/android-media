package com.tokopedia.media.picker.helper.utils

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object VideoGenerator {

    const val VIDEO_FILES_COUNT = 4

    private val mockVideoFileNameList = listOf(
        "test_mp_videos.mp4"
    )

    fun getFiles(context: Context): List<File> {
        return mockVideoFileNameList.map {
            streamToFile(context, it, it)
        }
    }

    fun getMultipleFiles(context: Context): List<File>{
        val videoCollection = ArrayList<File>()
        for(i in 0 until VIDEO_FILES_COUNT){
            videoCollection.add(
                streamToFile(context, sourceFileName = mockVideoFileNameList.first(), "test_mp_videos_$i.mp4")
            )
        }

        return videoCollection
    }

    private fun streamToFile(context: Context, sourceFileName: String, copyFileName: String): File {
        val inputStream = context.resources.assets.open(sourceFileName)

        // save similar file name into cache dir
        val outputFile = File(context.externalCacheDir, copyFileName)

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