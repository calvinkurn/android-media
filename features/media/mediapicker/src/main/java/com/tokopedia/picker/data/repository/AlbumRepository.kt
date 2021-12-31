package com.tokopedia.picker.data.repository

import android.content.Context
import android.net.Uri
import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.loader.LoaderDataSource
import com.tokopedia.picker.ui.PickerParam
import java.io.File

interface AlbumRepository {
    suspend operator fun invoke(param: PickerParam): List<Album>
}

class AlbumRepositoryImpl constructor(
    context: Context
) : LoaderDataSource(context), AlbumRepository {

    override suspend operator fun invoke(param: PickerParam): List<Album> {
        val cursor = query(param, -1) ?: error("cannot find the query")
        val albumMap = mutableMapOf<String, Album>()

        var recentPreviewUri: Uri? = null
        var recentMediaSize = 0

        if (cursor.moveToFirst()) {
            do {
                val media = medias(cursor, param) ?: continue
                if (recentPreviewUri == null) recentPreviewUri = media.uri

                val bucketId = cursor.getLong(projection[3])
                var bucket = cursor.getString(projection[2])

                if (bucket == null) {
                    bucket = File(media.path).parentFile.let {
                        it?.name
                    }?: DEFAULT_ALBUM_NAME
                }

                val album = albumMap[bucket]

                if (album == null) {
                    albumMap[bucket] = Album(
                        id = bucketId,
                        name = bucket,
                        preview = media.uri,
                        count = 0
                    )
                }

                album?.medias?.add(media)
            } while (cursor.moveToNext())
        }

        cursor.close()

        return albumMap.values
            .map {
                val totalInAlbum = it.medias.size + 1

                // get total of media
                recentMediaSize += totalInAlbum

                // return the map
                it.copy(count = totalInAlbum)
            }
            .toMutableList()
            .also {
                val firstItemIndex = 0

                // add the recent album at the first of item
                it.add(firstItemIndex, Album(
                    id = RECENT_ALBUM_ID,
                    name = RECENT_ALBUM_NAME,
                    preview = recentPreviewUri,
                    count = recentMediaSize
                ))
            }
    }

    companion object {
        private const val DEFAULT_ALBUM_NAME = "SDCARD"
        private const val RECENT_ALBUM_NAME = "Recent"

        const val RECENT_ALBUM_ID = -1L
    }

}