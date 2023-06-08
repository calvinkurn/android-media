package com.tokopedia.media.editor.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.editor.ui.uimodel.BitmapCreation
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
import com.tokopedia.picker.common.utils.wrapper.PickerFile.Companion.asPickerFile
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.FileChannel
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        bitmapParam: Bitmap,
        filename: String? = null,
        sourcePath: String
    ): File?

    fun saveToGallery(
        imageList: List<String>,
        onFinish: (result: List<String>?, error: Exception?) -> Unit
    )

    fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String
}

class SaveImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bitmapConverter: BitmapConverterRepository,
    private val bitmapCreation: BitmapCreationRepository,
) : SaveImageRepository {
    override fun saveToCache(
        bitmapParam: Bitmap,
        filename: String?,
        sourcePath: String
    ): File? {
        val isPng = ImageProcessingUtil.isPng(sourcePath)
        return ImageProcessingUtil.writeImageToTkpdPath(
            bitmapParam,
            if (isPng) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG,
            getEditorSaveFolderPath()
        )
    }

    override fun saveToGallery(
        imageList: List<String>,
        onFinish: (result: List<String>?, error: Exception?) -> Unit
    ) {
        val listResult = mutableListOf<String>()
        imageList.forEach {
            if (it.isEmpty()) {
                listResult.add("")
                return@forEach
            }

            val file = it.asPickerFile()
            if (!file.exists()) {
                onFinish(null, IOException("File ${file.absolutePath} not found"))
                return
            }

            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            var resultFile: File? = null

            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, MIME_IMAGE_TYPE)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val fileName = fileName(file.name)
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)

                val basePath =
                    ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES)

                try {
                    resultFile = File("${basePath.first().path}/$fileName")
                    resultFile.createNewFile()

                    // copy image to pictures dir
                    copyFile(file, resultFile)

                    contentValues.put(MediaStore.MediaColumns.DATA, resultFile.path)

                    context.contentResolver.insert(contentUri, contentValues)
                } catch (e: Exception) {
                    onFinish(null, e)
                    return
                }
            } else {
                val fileName = fileName(file.nameWithoutExtension)
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)

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
                        } catch (e: Exception) {
                            onFinish(null, e)
                            return
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

            listResult.add(resultFile?.path ?: "")
        }


        onFinish(listResult, null)
    }

    override fun flattenImage(
        imageBaseUrl: String,
        imageAddedUrl: String,
        sourcePath: String
    ): String {
        val latch = CountDownLatch(1)

        var resultBitmap: Bitmap? = null
        Thread {
            bitmapConverter.uriToBitmap(Uri.parse(imageBaseUrl))?.let { baseBitmap ->
                resultBitmap = baseBitmap

                bitmapConverter.uriToBitmap(Uri.parse(imageAddedUrl))?.let { overlayBitmap ->
                    val widthValidation = baseBitmap.width != overlayBitmap.width
                    val heightValidation = baseBitmap.height != overlayBitmap.height

                    val finalBitmap = if (widthValidation || heightValidation) {
                        bitmapCreation.createBitmap(
                            BitmapCreation.scaledBitmap(
                                overlayBitmap,
                                baseBitmap.width,
                                baseBitmap.height,
                                true
                            )
                        )
                    } else {
                        overlayBitmap
                    }

                    val canvas = Canvas(baseBitmap)
                    finalBitmap?.let {
                        canvas.drawBitmap(it,
                            XY_FLATTEN_COORDINATE,
                            XY_FLATTEN_COORDINATE,
                            Paint()
                        )
                    }
                }
            }

            latch.countDown()
        }.start()

        latch.await()
        return resultBitmap?.let {
            saveToCache(it, sourcePath = sourcePath)?.path ?: ""
        } ?: ""
    }

    private fun fileName(name: String): String {
        return "${FILE_NAME_PREFIX}_$name"
    }

    @Throws(IOException::class)
    private fun copyFile(src: File?, dst: File?) {
        var inChannel: FileChannel? = null
        var outChannel: FileChannel? = null
        try {
            inChannel = FileInputStream(src).channel
            outChannel = FileOutputStream(dst).channel
            inChannel.transferTo(0, inChannel.size(), outChannel)
        } finally {
            inChannel?.close()
            outChannel?.close()
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

    companion object {
        private const val FILE_NAME_PREFIX = "Tkpd"
        private const val MIME_IMAGE_TYPE = "image/jpeg"

        private const val XY_FLATTEN_COORDINATE = 0f
    }
}
