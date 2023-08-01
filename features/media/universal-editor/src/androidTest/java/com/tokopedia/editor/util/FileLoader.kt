package com.tokopedia.editor.util

import android.content.Context
import java.io.File
import java.io.FileOutputStream

object FileLoader {

    private const val IMAGE = "sample_image.jpeg"
    private const val VIDEO = "sample_video.mp4"

    fun imageFile(context: Context): File {
        return streamFile(context, IMAGE)
    }

    fun videoFile(context: Context): File {
        return streamFile(context, VIDEO)
    }

    private fun streamFile(context: Context, fileName: String): File {
        val `is` = context.resources.assets.open(fileName)
        val output = File(context.externalCacheDir, fileName)

        `is`.use {
            val os = FileOutputStream(output)

            os.use { output ->
                val buffer = ByteArray(2 * 1024)

                while (true) {
                    val count = `is`.read(buffer)
                    if (count < 0) break

                    output.write(buffer, 0, count)
                }

                output.flush()
            }
        }

        return output
    }
}
