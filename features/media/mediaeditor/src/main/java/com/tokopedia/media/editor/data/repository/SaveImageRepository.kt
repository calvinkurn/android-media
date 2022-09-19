package com.tokopedia.media.editor.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
import com.tokopedia.media.loader.loadImageWithEmptyTarget
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.picker.common.utils.isImageFormat
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.file.PublicFolderUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel
import javax.inject.Inject
import kotlin.random.Random

interface SaveImageRepository {
    fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String? = null,
    ): File?

    fun clearEditorCache()
    fun saveToGallery(
        context: Context,
        imageList: List<String>,
        onFinish: (result: List<String>) -> Unit
    )
}

class SaveImageRepositoryImpl @Inject constructor() : SaveImageRepository {
    override fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String?
    ): File? {
        return ImageProcessingUtil.writeImageToTkpdPath(
            bitmapParam,
            Bitmap.CompressFormat.JPEG,
            getEditorSaveFolderPath()
        )
    }

    override fun clearEditorCache() {
        FileUtil.deleteFolder(
            FileUtil.getTokopediaInternalDirectory(getEditorSaveFolderPath()).absolutePath
        )
    }

    override fun saveToGallery(
        context: Context,
        imageList: List<String>,
        onFinish: (result: List<String>) -> Unit
    ) {
        val listResult = mutableListOf<String>()
        imageList.forEach {
            val file = it.asPickerFile()

            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            var resultFile: File? = null
            var fileName = fileName(file.name)

            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_IMAGE_TYPE)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val basePath =
                    ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES)
                resultFile = File("${basePath[0].path}/$fileName")
                resultFile.createNewFile()

                // copy image to pictures dir
                copyFile(file, resultFile)

                contentValues.put(MediaStore.MediaColumns.DATA, resultFile.path)

                context.contentResolver.insert(contentUri, contentValues)
            } else {
                contentValues.put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES
                )

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

                        FileUtil.getPath(context.contentResolver, uriResult)?.let {
                            resultFile = File(it)
                        }
                    }
                }
            }

            listResult.add(resultFile?.path ?: "")
        }

        onFinish(listResult)
    }

    private fun fileName(name: String): String {
        return "${FILE_NAME_PREFIX}_$name"
    }

    @Throws(IOException::class)
    private fun copyFile(src: File?, dst: File?) {
        val inChannel: FileChannel = FileInputStream(src).channel
        val outChannel: FileChannel? = FileOutputStream(dst).channel
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel.close()
            outChannel?.close()
        }
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

    companion object {
        private const val FILE_NAME_PREFIX = "Tkpd"
        private const val MIME_IMAGE_TYPE = "image/jpeg"
    }
}
