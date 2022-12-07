package com.tokopedia.picker.common.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.picker.common.PICKER_SAVE_GALLERY
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.file.FileUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel

object SaveImageProcessor {
    private const val FILE_NAME_PREFIX = "Tkpd"
    private const val MIME_IMAGE_TYPE = "image/jpeg"
    private const val MIME_VIDEO_TYPE = "video/mp4"

    fun saveToGallery(context: Context, filePath: String): File? {
        try {
            if (filePath.isEmpty()) return null

            val file = filePath.asPickerFile()

            val contentUri: Uri
            val mimeType: String
            if (file.isImage()) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                mimeType = MIME_IMAGE_TYPE
            } else {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                mimeType = MIME_VIDEO_TYPE
            }

            var resultFile: File? = null
            val fileName = fileName(file)

            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)

            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val basePath = getBasePath(context, file.isImage())

                resultFile = File("${basePath.first().path}/$fileName")
                resultFile.createNewFile()

                // copy image to pictures dir
                copyFile(file, resultFile)

                contentValues.put(MediaStore.MediaColumns.DATA, resultFile.path)

                context.contentResolver.insert(contentUri, contentValues)
            } else {
                val mediaContentValue = getQContentValue(file.isImage())
                contentValues.put(mediaContentValue.first, mediaContentValue.second)

                context.contentResolver.insert(contentUri, contentValues)?.let { uriResult ->
                    context.contentResolver.openOutputStream(uriResult)?.let { outputStream ->
                        var inputStream: FileInputStream? = null
                        try {
                            inputStream = FileInputStream(file)
                            copy(inputStream, outputStream)
                        } finally {
                            inputStream?.close()
                            outputStream.close()
                        }

                        FileUtil.getPath(context.contentResolver, uriResult)?.let { resultPath ->
                            val tempResultFile = File(resultPath)
                            val renamedResultFile = File(fileName)

                            tempResultFile.renameTo(renamedResultFile)

                            resultFile = tempResultFile
                        }
                    }
                }
            }

            return resultFile
        } catch (e: Exception) {
            ServerLogger.log(Priority.P2, PICKER_SAVE_GALLERY, mapOf("Error" to e.toString()))
            return null
        }
    }

    private fun getBasePath(context: Context, isImage: Boolean): Array<File> {
        return if (isImage) {
            ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES)
        } else {
            ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_MOVIES)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getQContentValue(isImage: Boolean): Pair<String, String> {
        return if (isImage) {
            Pair(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        } else {
            Pair(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
        }
    }

    @Throws(IOException::class)
    private fun copyFile(src: File?, dst: File?) {
        val inChannel: FileChannel = FileInputStream(src).channel
        val outChannel: FileChannel = FileOutputStream(dst).channel
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel.close()
            outChannel.close()
        }
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(source.available())
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    private fun fileName(file: PickerFile): String {
        val fileName = "${FILE_NAME_PREFIX}_${file.nameWithoutExtension}"
        val extension = file.extension

        return "$fileName.$extension"
    }
}
