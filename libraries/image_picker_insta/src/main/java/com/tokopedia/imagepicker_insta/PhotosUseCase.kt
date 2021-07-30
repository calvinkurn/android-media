package com.tokopedia.imagepicker_insta

import android.content.Context
import com.tokopedia.imagepicker_insta.models.Asset
import javax.inject.Inject

class PhotosUseCase @Inject constructor() {
    val importer = PhotoImporter()

    fun getPhotos(context: Context): List<Asset> {
        return importer.importPhotos(context)
    }
}