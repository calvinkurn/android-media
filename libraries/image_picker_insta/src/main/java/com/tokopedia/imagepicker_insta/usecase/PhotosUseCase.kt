package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import com.tokopedia.imagepicker_insta.PhotoImporter
import com.tokopedia.imagepicker_insta.models.FolderData
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.PhotosImporterData
import com.tokopedia.imagepicker_insta.util.StorageUtil
import javax.inject.Inject

class PhotosUseCase @Inject constructor() {
    private val importer = PhotoImporter()

    fun getPhotosFromMediaStorage(context: Context): PhotosImporterData {

        val photosImporterData = importer.importPhotos(context)
        val internalPhotos = importer.importPhotosFromInternalDir(context)
        val internalPhotosImageAdapterData = internalPhotos.map {
            ImageAdapterData(it)
        }
        if(internalPhotos.isNotEmpty()){
            photosImporterData.imageAdapterDataList.addAll(internalPhotosImageAdapterData)
            photosImporterData.folders.add(FolderData(StorageUtil.INTERNAL_FOLDER_NAME,importer.getSubtitle(internalPhotos.size),internalPhotos.first().contentUri) )
            photosImporterData.imageAdapterDataList.sortByDescending {
                it.asset.createdDate
            }
        }
        return photosImporterData
    }
}