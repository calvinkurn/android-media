package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.mediaImporter.VideoImporter
import com.tokopedia.imagepicker_insta.models.FolderData
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.models.MediaUseCaseData
import com.tokopedia.imagepicker_insta.util.StorageUtil
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class PhotosUseCase @Inject constructor() {
    private val photosImporter = PhotoImporter()
    private val videosImporter = VideoImporter()

    private fun getFolderDataListFromFolders(folderList: Set<String>, imageAdapterDataList: List<ImageAdapterData>): ArrayList<FolderData> {

        val tempFoldersList = arrayListOf<FolderData>()
        folderList.forEach { folderName ->
            val media = imageAdapterDataList.first {
                it.asset.folder == folderName
            }
            val totalPhotos = imageAdapterDataList.filter {
                it.asset.folder == folderName
            }.size

            tempFoldersList.add(FolderData(folderName, getSubtitle(totalPhotos), media.asset.contentUri))
        }

        if (imageAdapterDataList.isNotEmpty()) {
            tempFoldersList.add(0, FolderData(PhotoImporter.ALL, getSubtitle(imageAdapterDataList.size), imageAdapterDataList.first().asset.contentUri))
        }
        return tempFoldersList
    }

    private fun getSubtitle(mediaCount: Int): String {
        if (mediaCount == 1) {
            return "$mediaCount media"
        } else {
            return "$mediaCount medias"
        }
    }

    private fun sortAdapterDataList(arrayList: ArrayList<ImageAdapterData>) {

        //sort in descending order
        arrayList.sortWith(kotlin.Comparator { i1, i2 ->
            val date1 = Date(i1.asset.createdDate)
            val date2 = Date(i2.asset.createdDate)
            if (date1.before(date2)) {
                return@Comparator 1
            }else if (date1 == date2){
                return@Comparator 0
            }
            return@Comparator -1
        })
    }

    fun getAssetsFromMediaStorage(context: Context): MediaUseCaseData {
        val photosData = photosImporter.importMedia(context)
        val videosData = videosImporter.importMedia(context)

        val combinedFoldersSet = (photosData.folderSet).toHashSet()
        combinedFoldersSet.addAll(videosData.folderSet)

        val combinedAdapterDataList = ArrayList(photosData.imageAdapterDataList)
        combinedAdapterDataList.addAll(videosData.imageAdapterDataList)
        sortAdapterDataList(combinedAdapterDataList)

        val combinedFolderDataList = getFolderDataListFromFolders(combinedFoldersSet, combinedAdapterDataList)

        //Internal assets Logic
        val internalMediaList = photosImporter.importMediaFromInternalDir(context)
        internalMediaList.addAll(videosImporter.importMediaFromInternalDir(context))

        val internalMediaAdapterDataList = internalMediaList.map {
            ImageAdapterData(it)
        }

        if(internalMediaAdapterDataList.isNotEmpty()){
            combinedAdapterDataList.addAll(internalMediaAdapterDataList)

            combinedFolderDataList.add(
                FolderData(
                    StorageUtil.INTERNAL_FOLDER_NAME,
                    getSubtitle(internalMediaAdapterDataList.size),
                    internalMediaList.first().contentUri
                )
            )
        }

        val mediaImporterData = MediaImporterData(combinedAdapterDataList, combinedFoldersSet)
        return MediaUseCaseData(mediaImporterData, combinedFolderDataList)
    }
}