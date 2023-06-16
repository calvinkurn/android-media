package com.tokopedia.media.editor.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        bitmapParam: Bitmap,
        filename: String? = null,
        sourcePath: String
    ): File?
}

class SaveImageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
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
}
