package com.tokopedia.imagepicker_insta.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.util.AlbumUtil
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class PhotosUseCase @Inject constructor() {
    private val photosImporter = PhotoImporter()

    fun createAssetsFromFile(file: File, context: Context, queryConfiguration: QueryConfiguration): Asset? {
        if (photosImporter.isImageFile(file.absolutePath)) {
            return photosImporter.createPhotosDataFromInternalFile(file)
        } else {
            val videoMetaData = photosImporter.getVideoMetaData(file.absolutePath, context)
            if (videoMetaData.isSupported) {
                return photosImporter.createVideosDataFromInternalFile(file, videoMetaData.duration, queryConfiguration.videoMaxDuration)
            }
        }
        return null
    }

    fun getDateFromUri(uri: Uri, context: Context): Long {
        return when (uri.scheme) {
            ContentResolver.SCHEME_CONTENT -> photosImporter.getDateFromContentUri(uri, context)
            ContentResolver.SCHEME_FILE -> photosImporter.getCreateAtForInternalFile(File(uri.path!!))
            else -> 0L
        }
    }

    fun getFolderData(context: Context): List<FolderData> {
        val internalFolderList = photosImporter.getInternalMediaAlbum(context)
        val externalFolderList = photosImporter.getExternalMediaAlbums(context)
        val finalFolderList = ArrayList(externalFolderList)
        finalFolderList.addAll(internalFolderList)

        if (finalFolderList.isEmpty()) return emptyList()

        val latestUri: Uri

        if (internalFolderList.isEmpty()) {
            latestUri = externalFolderList.first().thumbnailUri
        } else if (externalFolderList.isEmpty()) {
            latestUri = internalFolderList.first().thumbnailUri
        } else {

            val createdDataInternalFile = getDateFromUri(internalFolderList.first().thumbnailUri, context)
            val createdDataExternalFile = getDateFromUri(externalFolderList.first().thumbnailUri, context)

            latestUri = if (createdDataExternalFile > createdDataInternalFile) externalFolderList.first().thumbnailUri
            else internalFolderList.first().thumbnailUri
        }

        var totalMediaCount = 0
        finalFolderList.forEach {
            totalMediaCount += it.itemCount
        }

        finalFolderList.add(
            PhotoImporter.INDEX_OF_RECENT_MEDIA_IN_FOLDER_LIST,
            FolderData(
                AlbumUtil.RECENTS,
                CameraUtil.getMediaCountText(totalMediaCount),
                latestUri,
                totalMediaCount
            )
        )

        return finalFolderList
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

    suspend fun getMediaByFolderNameFlow(folderName: String, context: Context, queryConfiguration: QueryConfiguration): Flow<MediaUseCaseData> {
        return flow {

            val internalAssets = if (folderName == StorageUtil.INTERNAL_FOLDER_NAME || folderName == AlbumUtil.RECENTS)
                ArrayList(photosImporter.importMediaFromInternalDir(context, queryConfiguration))
            else
                ArrayList()

            /**
             * 1. find index of asset in internal_assets which date is just less than external_asset_list - let's say the
             * index is internal_file_index
             * 2. sort two arrayList
             *      a. sublist(0,internal_file_index)
             *      b. external_asset_list
             * 3. internal_file_index is negative - ignore sorting with internal_assets
             * */
            photosImporter.importPhotoVideoFlow(context, folderName, queryConfiguration).collect { importedMediaMetaData ->

                if (internalAssets.isEmpty()) {
                    emit(MediaUseCaseData(importedMediaMetaData.mediaImporterData))

                } else if (importedMediaMetaData.mediaImporterData.imageAdapterDataList.isEmpty() && internalAssets.isNotEmpty()) {
                    val list = ArrayList<ImageAdapterData>()
                    list.addAll(internalAssets.map { ImageAdapterData(it) })

                    val mediaImportedData = MediaImporterData(list)
                    emit(MediaUseCaseData(mediaImportedData))

                } else {
                    val maxCreatedDataForExternalAsset = importedMediaMetaData.mediaImporterData.imageAdapterDataList.last().asset.createdDate

                    var internalAssetsIndex = -1
                    val internalAssetsSize = internalAssets.size
                    internalAssets.asReversed().forEachIndexed { index, item ->
                        if (item.createdDate <= maxCreatedDataForExternalAsset) {
                            internalAssetsIndex = index
                            return@forEachIndexed
                        }
                    }

                    val allInternalAssetsAreClickedAfterExternalAssets = internalAssetsIndex == -1
                    val subList = ArrayList<Asset>()
                    if (allInternalAssetsAreClickedAfterExternalAssets) {
                        subList.addAll(internalAssets)
                        internalAssets.clear()

                    } else {
                        subList.addAll(internalAssets.subList(internalAssetsIndex, internalAssetsSize))
                        internalAssets.removeAll(subList)
                    }

                    val internalAdapterList = subList.map { ImageAdapterData(it) }

                    val finalList = ArrayList(importedMediaMetaData.mediaImporterData.imageAdapterDataList)

                    if (!allInternalAssetsAreClickedAfterExternalAssets) {

                        finalList.addAll(internalAdapterList)
                        sortAdapterDataList(finalList)
                    } else {
                        finalList.addAll(0, internalAdapterList)
                        sortAdapterDataList(finalList)
                    }
                    internalAssetsIndex = -1
                    emit(MediaUseCaseData(MediaImporterData(finalList)))
                }
            }
        }
    }

    fun getUriSetFromImageAdapterData(list: List<ImageAdapterData>): HashSet<Uri> {
        val uriSet = HashSet<Uri>()
        list.forEach {
            uriSet.add(it.asset.contentUri)
        }
        return uriSet
    }
}