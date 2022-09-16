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

            val resultPath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                PublicFolderUtil.putFileToPublicFolder(
                    context = context,
                    localFile = file,
                    mimeType = mimeType(file),
                    outputFileName = fileName(file.name)
                ).first?.path
            } else {
                val basePath = ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES)
                val fileName = fileName("semut_"+file.name)
                val resultFile = File("${basePath[0]}/$fileName")
                resultFile.createNewFile()

                // copy image to pictures dir
                copyFile(file, resultFile)

                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    contentValues.put(
                        MediaStore.Images.Media.RELATIVE_PATH,
                        Environment.DIRECTORY_PICTURES
                    )
                } else {
                    contentValues.put(MediaStore.MediaColumns.DATA, resultFile.path);
                }

                // add new picture on dir to media table
                val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                context.contentResolver.insert(contentUri, contentValues)

                resultFile.path
            }

            resultPath?.let { pathString ->
                listResult.add(pathString)
            }
        }

        onFinish(listResult)
    }

    private fun recursiveSaveToGallery(
        context: Context,
        index: Int,
        sourceList: List<String>,
        resultList: MutableList<String>,
        onFinish: (result: List<String>) -> Unit
    ) {
        if (index > sourceList.size - 1) {
            onFinish(resultList)
            return
        }

        val imagePath = sourceList[index]
        if (isImageFormat(imagePath)) {
            val resultPath = MediaStore.Images.Media.insertImage(
                context.contentResolver,
                sourceList[index],
                "Title-${Random.nextInt(1000)}",
                "Desc"
            )

            FileUtil.getPath(context.contentResolver, Uri.parse(resultPath))?.let {
                resultList.add(it)
            }
        }

        recursiveSaveToGallery(context, index + 1, sourceList, resultList, onFinish)
    }

    private fun mimeType(file: PickerFile): String {
        return when {
            file.isVideo() -> MIME_VIDEO_TYPE
            file.isImage() -> MIME_IMAGE_TYPE
            else -> ""
        }
    }

    private fun fileName(name: String): String {
        val currentTime = System.currentTimeMillis()
        return "${FILE_NAME_PREFIX}_${currentTime}_$name"
    }

    @Throws(IOException::class)
    fun copyFile(src: File?, dst: File?) {
        val inChannel: FileChannel = FileInputStream(src).channel
        val outChannel: FileChannel? = FileOutputStream(dst).channel
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel.close()
            outChannel?.close()
        }
    }

    companion object {
        private const val FILE_NAME_PREFIX = "Tkpd_"

        private const val MIME_VIDEO_TYPE = "video/mp4"
        private const val MIME_IMAGE_TYPE = "image/jpeg"
    }
}
