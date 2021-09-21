package com.tokopedia.imagepicker_insta.usecase

import android.content.Context
import android.net.Uri
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.mediaImporter.VideoImporter
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.util.StorageUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class PhotosUseCase @Inject constructor() {
    private val photosImporter = PhotoImporter()
    private val videosImporter = VideoImporter()

    fun createAssetsFromFile(file: File, context: Context): Asset? {
        if (photosImporter.isImageFile(file.absolutePath)) {
            return photosImporter.createPhotosDataFromInternalFile(file)
        } else {
            val videoMetaData = videosImporter.getViewMetaData(file.absolutePath, context)
            if (videoMetaData.isSupported) {
                return videosImporter.createVideosDataFromInternalFile(file, videoMetaData.duration)
            }
        }
        return null
    }

    private fun getFolderDataListFromFolders(folderList: Set<String>, imageAdapterDataList: List<ImageAdapterData>): ArrayList<FolderData> {

        val tempFoldersList = arrayListOf<FolderData>()
        folderList.forEach { folderName ->
            val media = imageAdapterDataList.first {
                it.asset.folder == folderName
            }
            val totalPhotos = imageAdapterDataList.filter {
                it.asset.folder == folderName
            }.size

            tempFoldersList.add(FolderData(folderName, getSubtitle(totalPhotos), media.asset.contentUri, totalPhotos))
        }

        if (imageAdapterDataList.isNotEmpty()) {
            tempFoldersList.add(
                0,
                FolderData(
                    PhotoImporter.ALL,
                    getSubtitle(imageAdapterDataList.size),
                    imageAdapterDataList.first().asset.contentUri,
                    imageAdapterDataList.size
                )
            )
        }
        return tempFoldersList
    }

    fun getSubtitle(mediaCount: Int): String {
        return "$mediaCount media"
    }

    private fun sortAdapterDataList(arrayList: ArrayList<ImageAdapterData>) {

        //sort in descending order
        arrayList.sortWith(kotlin.Comparator { i1, i2 ->
            val date1 = Date(i1.asset.createdDate)
            val date2 = Date(i2.asset.createdDate)
            if (date1.before(date2)) {
                return@Comparator 1
            } else if (date1 == date2) {
                return@Comparator 0
            }
            return@Comparator -1
        })
    }

    suspend fun getAssetsFromMediaStorage(context: Context): MediaUseCaseData {
        return supervisorScope {

            val internalPhotosAsync = async {
                photosImporter.importMediaFromInternalDir(context)
            }
            val internalVideosAsync = async { videosImporter.importMediaFromInternalDir(context) }

            val photosAsync = async { photosImporter.importMedia(context) }
            val videosAsync = async { videosImporter.importMedia(context) }

            val internalMediaList = internalPhotosAsync.await()
            internalMediaList.addAll(internalVideosAsync.await())

            val internalMediaAdapterDataList = internalMediaList.map {
                ImageAdapterData(it)
            }

            val photosData = photosAsync.await()

            val videosData = videosAsync.await()

            val combinedFoldersSet = HashSet(photosData.folderSet)
            combinedFoldersSet.addAll(videosData.folderSet)

            val combinedAdapterDataList = ArrayList(photosData.imageAdapterDataList)
            combinedAdapterDataList.addAll(videosData.imageAdapterDataList)

            val combinedFolderDataList = getFolderDataListFromFolders(combinedFoldersSet, combinedAdapterDataList)

            if (internalMediaAdapterDataList.isNotEmpty()) {
                combinedAdapterDataList.addAll(internalMediaAdapterDataList)

                combinedFolderDataList.add(
                    FolderData(
                        StorageUtil.INTERNAL_FOLDER_NAME,
                        getSubtitle(internalMediaAdapterDataList.size),
                        internalMediaList.first().contentUri,
                        internalMediaAdapterDataList.size
                    )
                )
            }
            sortAdapterDataList(combinedAdapterDataList)

            val mediaImporterData = MediaImporterData(combinedAdapterDataList, combinedFoldersSet)

            val uriSet = HashSet<Uri>()
            combinedAdapterDataList.forEach {
                uriSet.add(it.asset.contentUri)
            }
            return@supervisorScope MediaUseCaseData(mediaImporterData, combinedFolderDataList, uriSet = uriSet)
        }
    }
}