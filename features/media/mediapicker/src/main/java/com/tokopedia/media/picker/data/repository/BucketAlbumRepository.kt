@file:Suppress("PII Data Exposure")
package com.tokopedia.media.picker.data.repository

import android.database.Cursor
import android.net.Uri
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.media.picker.data.MediaQueryDataSource
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl.Companion.BUCKET_ALL_MEDIA_ID
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.entity.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface BucketAlbumRepository {
    operator fun invoke(): Flow<List<Album>>
}

class BucketAlbumRepositoryImpl @Inject constructor(
    source: MediaQueryDataSource
) : BucketAlbumRepository, MediaQueryDataSource by source {

    override operator fun invoke(): Flow<List<Album>> {
        return flow {
            try {
                val cursor = setupMediaQuery(BUCKET_ALL_MEDIA_ID)
                val albumMap = mutableMapOf<Long, Album>()
                var mediaTotal = 1

                if (cursor?.moveToFirst() == true) {
                    do {
                        val media = createMedia(cursor)?: continue
                        val bucketName = getOrSetBucketName(cursor, media)
                        val bucketId = getBucketId(cursor)

                        val album = albumMap[bucketId]

                        if (album == null) {
                            if (media.file.exists().not()) continue

                            albumMap[bucketId] = Album(
                                bucketId,
                                bucketName,
                                media.uri
                            )
                        }

                        if (album != null) {
                            // media total by bucketId
                            album.count++

                            // calculate all media items
                            mediaTotal++
                        }
                    } while (cursor.moveToNext())
                }

                cursor?.close()

                val result = albumMap.values
                    .toMutableList()
                    .also {
                        it.add(Int.ZERO, recentAlbumItem(it.firstOrNull()?.uri, mediaTotal))
                    }

                emit(result)
            } catch (t: Throwable) {
                emit(emptyList())
            }
        }
    }

    private fun recentAlbumItem(uri: Uri?, total: Int) = Album(
        id = BUCKET_ALL_MEDIA_ID,
        name = RECENT_ALBUM_NAME,
        uri = uri,
        count = total
    )

    private fun getOrSetBucketName(cursor: Cursor, media: Media): String {
        var bucketName = cursor.getString(cursor.columnIndex("bucket_name"))

        if (bucketName == null) {
            bucketName = media.file.parentFile?.name?: DEFAULT_ALBUM_NAME
        }

        return bucketName
    }

    private fun getBucketId(cursor: Cursor): Long {
        return cursor.getLong(cursor.columnIndex("bucket_id"))
    }

    companion object {
        private const val DEFAULT_ALBUM_NAME = "SDCARD"
        private const val RECENT_ALBUM_NAME = "Semua Media"
    }
}
