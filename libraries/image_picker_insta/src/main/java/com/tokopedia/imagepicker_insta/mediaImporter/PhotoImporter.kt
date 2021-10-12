package com.tokopedia.imagepicker_insta.mediaImporter

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.util.AlbumUtil
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.util.VideoUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File

class PhotoImporter : MediaImporter {

    companion object {
        const val ALL = "Recents"
        const val INDEX_OF_RECENT_MEDIA_IN_FOLDER_LIST = 0
        private val URI = MediaStore.Files.getContentUri("external")
        private val BATCH_LIMIT = 1000

    }

    private fun getSelectionStringBuilder(folderName: String? = null): StringBuilder {
        val selectionBuilder = StringBuilder()
        selectionBuilder.append("(")
        selectionBuilder.append(MediaStore.Files.FileColumns.MEDIA_TYPE)
        selectionBuilder.append("=")
        selectionBuilder.append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        selectionBuilder.append(" OR ")
        selectionBuilder.append(MediaStore.Files.FileColumns.MEDIA_TYPE)
        selectionBuilder.append("=")
        selectionBuilder.append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
        selectionBuilder.append(")")
        selectionBuilder.append(" AND ")
        selectionBuilder.append(" ${MediaStore.Images.Media.SIZE} > 10 ")

        if (!folderName.isNullOrEmpty() && folderName != AlbumUtil.RECENTS) {
            selectionBuilder.append(" AND ")
            selectionBuilder.append(" ${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ")
            selectionBuilder.append("=")
            selectionBuilder.append("'$folderName'")
        }

        return selectionBuilder
    }

    fun getInternalMediaAlbum(context: Context): List<FolderData> {
        val list = arrayListOf<FolderData>()
        val directory = File(context.filesDir, StorageUtil.INTERNAL_FOLDER_NAME)

        if (directory.exists() && directory.isDirectory) {

            val supportedFileList = directory.listFiles()?.filter {
                val filePath = it.absolutePath
                isImageFile(filePath) || isVideoFile(filePath)
            }
            if (!supportedFileList.isNullOrEmpty()) {
                val folderData = FolderData(
                    StorageUtil.INTERNAL_FOLDER_NAME,
                    CameraUtil.getMediaCountText(supportedFileList.size),
                    Uri.fromFile(supportedFileList.first()),
                    supportedFileList.size
                )
                list.add(folderData)
            }
        }
        return list
    }

    fun getDateFromContentUri(uri: Uri, context: Context): Long {
        if (uri.scheme != ContentResolver.SCHEME_CONTENT) throw Exception("Should be content uri")
        val projections = arrayOf(MediaStore.Files.FileColumns.DATE_ADDED)
        val cur = context.contentResolver.query(uri, projections, null, null, null)
        var dateAdded = 0L
        try {
            if (cur != null && cur.count > 0) {
                if (cur.moveToNext()) {
                    do {
                        val dateAddedIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
                        dateAdded = cur.getLong(dateAddedIndex)
                    } while (cur.moveToNext())
                }
            }

        } catch (th: Throwable) {
            Timber.e(th)
        } finally {
            cur?.close()
        }
        return dateAdded
    }

    fun getExternalMediaAlbums(context: Context): List<FolderData> {
        val projections = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Files.FileColumns.MEDIA_TYPE
        )

        val cur = context.contentResolver.query(URI, projections, getSelectionStringBuilder().toString(), null, null)

        val albumMap = mutableMapOf<String, Int>()
        val albumThumbnailMap = mutableMapOf<String, Uri>()
        try {
            if (cur != null && cur.count > 0) {
                if (cur.moveToNext()) {
                    do {
                        val idIndex = cur.getColumnIndex(MediaStore.Images.Media._ID)
                        val folderIndex = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                        val mediaTypeIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)

                        val index = cur.getLong(idIndex)
                        var folderName = cur.getString(folderIndex)
                        if (folderName.isNullOrEmpty()) {
                            folderName = "Others"
                        }

                        val contentUri = when (cur.getInt(mediaTypeIndex)) {
                            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> ContentUris.withAppendedId(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                index
                            )
                            else -> ContentUris.withAppendedId(
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                index
                            )
                        }

                        val mediaInAlbum = albumMap[folderName]
                        if (mediaInAlbum == null) {
                            albumMap[folderName] = 1
                            albumThumbnailMap[folderName] = contentUri
                        } else {
                            albumMap[folderName] = mediaInAlbum + 1
                        }
                    } while (cur.moveToNext())
                }
            }
            return albumMap.filter {
                albumThumbnailMap[it.key] != null
            }.map {
                FolderData(it.key, CameraUtil.getMediaCountText(it.value), albumThumbnailMap[it.key]!!, it.value)
            }
        } catch (th: Throwable) {
            Timber.e(th)
        } finally {
            cur?.close()
        }
        return emptyList()
    }

    suspend fun importPhotoVideoFlow(context: Context, folderName: String? = null, queryConfiguration: QueryConfiguration): Flow<ImportedMediaMetaData> {
        return flow {
                val imageAdapterDataList = ArrayList<ImageAdapterData>()

                val projections = arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Video.Media.DURATION,
                    MediaStore.Images.Media._ID
                )
                val selectionBuilder = getSelectionStringBuilder(folderName)

                val cur: Cursor?

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    val bundle = Bundle()
                    bundle.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selectionBuilder.toString())
                    bundle.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Images.Media.DATE_ADDED))
                    bundle.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)

                    cur = context.contentResolver.query(URI, projections, bundle, null)
                } else {
                    val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
                    cur = context.contentResolver.query(URI, projections, selectionBuilder.toString(), null, sortOrder)
                }
                var lastIndex: Long? = null

                var processedItemCount = 0

                try {
                    if (cur != null) {
                        if (cur.count == 0) {
                            emit(ImportedMediaMetaData(MediaImporterData(ArrayList(imageAdapterDataList)), 0))
                        } else if (cur.count > 0) {
                            val totalMediaCount = cur.count

                            if (cur.moveToNext()) {
                                do {
                                    val dateAddedIndex = cur.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)

                                    val idIndex = cur.getColumnIndex(MediaStore.Images.Media._ID)
                                    val mediaTypeIndex = cur.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)

                                    val index = cur.getLong(idIndex)

                                    lastIndex = index

                                    val dateAdded = cur.getLong(dateAddedIndex)

                                    val asset = when (cur.getInt(mediaTypeIndex)) {
                                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> PhotosData(
                                            contentUri = ContentUris.withAppendedId(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                index
                                            ),
                                            createdDate = dateAdded
                                        )
                                        else -> {
                                            val durationIndex = cur.getColumnIndex(MediaStore.Video.Media.DURATION)
                                            val duration = cur.getLong(durationIndex)
                                            VideoData(
                                                contentUri = ContentUris.withAppendedId(
                                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                                    index
                                                ),
                                                createdDate = dateAdded,
                                                durationText = VideoUtil.getFormattedDurationText(duration),
                                                canBeSelected = VideoUtil.isVideoWithinLimit(duration, queryConfiguration.videoMaxDuration)
                                            )
                                        }

                                    }
                                    processedItemCount += 1

                                    val imageAdapterData = ImageAdapterData(asset)
                                    imageAdapterDataList.add(imageAdapterData)

                                    if ((processedItemCount % BATCH_LIMIT == 0) || processedItemCount == totalMediaCount) {
                                        emit(ImportedMediaMetaData(MediaImporterData(ArrayList(imageAdapterDataList)), lastIndex))
                                        imageAdapterDataList.clear()
                                    }

                                    if (processedItemCount > totalMediaCount) {
                                        throw Exception("processed media item count can never greater than total media")
                                    }
                                } while (cur.moveToNext())
                            }
                        }
                    }

                } catch (th: Throwable) {
                    Timber.e(th)
                } finally {
                    cur?.close()
                }
            }
    }
}
