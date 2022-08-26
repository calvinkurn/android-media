package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.media.editor.utils.getEditorSaveFolderDir
import com.tokopedia.utils.file.FileUtil
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

interface SaveImageRepository {
    fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String? = null
    ): File?

    fun clearEditorCache()
}

class SaveImageRepositoryImpl @Inject constructor() : SaveImageRepository {
    override fun saveToCache(
        context: Context,
        bitmapParam: Bitmap,
        filename: String?
    ): File? {
        return ImageProcessingUtil.writeImageToTkpdPath(
            bitmapParam,
            Bitmap.CompressFormat.PNG,
            getEditorSaveFolderDir()
        )
    }

    override fun clearEditorCache() {
        FileUtil.deleteFolder(getEditorSaveFolderDir())
    }
}