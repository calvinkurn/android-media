package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import com.tokopedia.imagepicker_insta.PhotoImporter
import com.tokopedia.imagepicker_insta.models.PhotosImporterData
import com.tokopedia.imagepicker_insta.util.StorageUtil
import javax.inject.Inject

class PhotosUseCase @Inject constructor() {
    private val importer = PhotoImporter()

    fun getPhotosFromMediaStorage(context: Context): PhotosImporterData {

        val photosImporterData = importer.importPhotos(context)
        val internalPhotos= importer.importPhotosFromInternalDir(context)
        if(internalPhotos.isNotEmpty()){
            photosImporterData.assets.addAll(internalPhotos)
            photosImporterData.folders.add(StorageUtil.INTERNAL_FOLDER_NAME)
            photosImporterData.assets.sortByDescending {
                it.createdDate
            }
        }
        return photosImporterData
    }
}