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

        if (cursor.moveToFirst()) {
            do {
                val media = medias(cursor, param) ?: continue
                if (recentPreviewUri == null) recentPreviewUri = media.uri

                val bucketId = cursor.getLong(projection[4])
                var bucket = cursor.getString(projection[3])

                if (bucket == null) {
                    bucket = File(media.path).parentFile.let {
                        it?.name
                    }?: DEFAULT_ALBUM_NAME
                }

                val album = albumMap[bucket]

                if (album == null) albumMap[bucket] = Album(bucketId, bucket, media.uri, 0)
                if (album != null) album.count++
            } while (cursor.moveToNext())
        }

        cursor.close()

        return albumMap.values
            .toList()
            .addRecentAlbumAtFirst(recentPreviewUri)
    }

    private fun List<Album>.addRecentAlbumAtFirst(
        firstMediaPreviewUri: Uri?
    ): List<Album> {
        var recentMediaSize = 0

        return this.map {
            val totalInAlbum = it.count + 1
            recentMediaSize += totalInAlbum

            it
        }
            .toMutableList()
            .also {
                val firstItemIndex = 0

                // add the recent album at the first of item
                it.add(firstItemIndex, Album(
                    id = RECENT_ALBUM_ID,
                    name = RECENT_ALBUM_NAME,
                    preview = firstMediaPreviewUri,
                    count = recentMediaSize
                ))
            }
    }

    companion object {
        private const val DEFAULT_ALBUM_NAME = "SDCARD"

        const val RECENT_ALBUM_NAME = "Semua Media"
        const val RECENT_ALBUM_ID = -1L
    }

}