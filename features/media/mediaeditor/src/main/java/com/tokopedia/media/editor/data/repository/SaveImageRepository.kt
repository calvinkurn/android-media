package com.tokopedia.media.editor.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
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
            getEditorSaveFolderPath()
        )
    }

    override fun clearEditorCache() {
        FileUtil.deleteFolder(
            FileUtil.getTokopediaInternalDirectory(getEditorSaveFolderPath()).absolutePath
        )
    }
}