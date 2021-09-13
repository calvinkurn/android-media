package com.tokopedia.createpost.view.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.OutputStream

object FileUtil {

    fun createFilePathFromUri(context: Context, uri: Uri): String {
        var fileExtension = ""
        fileExtension = getFileType(context.contentResolver.getType(uri) ?: "")
        val childFileName = "del_" + "${System.currentTimeMillis()}" + ".$fileExtension"
        val file = File(context.cacheDir, childFileName)

        val os: OutputStream? = file.outputStream()
        val inputStream = context.contentResolver.openInputStream(uri)
        if (os != null) {
            inputStream?.copyTo(os)
        }

        inputStream?.close()
        os?.close()

        return file.absolutePath
    }

    private fun getFileType(mimeType: String): String {
        val slashIndex = mimeType.indexOf("/")
        return mimeType.substring(slashIndex + 1, mimeType.length)
    }
}