package com.tokopedia.media.preview.data.repository

import android.content.Context
import com.tokopedia.picker.common.utils.SaveImageProcessor
import java.io.File

interface SaveToGalleryRepository {
    fun dispatch(filePath: String): File?
}

class SaveToGalleryRepositoryImpl constructor(
    private val context: Context
) : SaveToGalleryRepository {

    override fun dispatch(filePath: String): File? {
        return SaveImageProcessor.saveToGallery(context, filePath)
    }

}
