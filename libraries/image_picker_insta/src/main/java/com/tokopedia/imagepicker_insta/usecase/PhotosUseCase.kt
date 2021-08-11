package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import com.tokopedia.imagepicker_insta.PhotoImporter
import com.tokopedia.imagepicker_insta.models.PhotosImporterData
import javax.inject.Inject

class PhotosUseCase @Inject constructor() {
    val importer = PhotoImporter()

    fun getPhotos(context: Context): PhotosImporterData {
        return importer.importPhotos(context)
    }
}