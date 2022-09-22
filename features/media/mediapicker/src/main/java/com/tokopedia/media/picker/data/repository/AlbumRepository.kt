package com.tokopedia.media.picker.data.repository

import android.net.Uri
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.picker.common.base.BaseRepository
import java.io.File

open class AlbumRepository constructor(
    loaderDataSource: LoaderDataSource,
    dispatcher: CoroutineDispatchers
) : BaseRepository<Unit, List<Album>>(dispatcher.io)
    , LoaderDataSource by loaderDataSource {

    override fun execute(param: Unit): List<Album> {
        val cursor = query(-1) ?: return emptyList()
        val albumMap = mutableMapOf<Long, Album>()

        var recentPreviewUri: Uri? = null

        if (cursor.moveToFirst()) {
            do {
                val media = medias(cursor) ?: continue

                // get first album media for thumbnail
                if (recentPreviewUri == null) recentPreviewUri = media.uri

                val bucketId = cursor.getLong(cursor.getColumnIndex(projection[4])) // directory id
                var bucketName = cursor.getString(cursor.getColumnIndex(projection[3])) // directory name

                if (bucketName == null) {
                    bucketName = File(media.path).parentFile.let {
                        it?.name
                    }?: DEFAULT_ALBUM_NAME
                }

                val album = albumMap[bucketId]

                if (album == null) {
                    albumMap[bucketId] = Album(
                        bucketId,
                        bucketName,
                        media.uri,
                        1
                    )
                }

                if (album != null) {
                    album.count++
                }
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
        var recentMediaSize = 1

        return this.map {
            val totalInAlbum = it.count
            recentMediaSize += totalInAlbum

            it
        }.toMutableList().also {
            val firstItemIndex = 0

            // add the recent album at the first of item
            it.add(
                firstItemIndex, Album(
                    id = RECENT_ALBUM_ID,
                    name = RECENT_ALBUM_NAME,
                    preview = firstMediaPreviewUri,
                    count = recentMediaSize
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_ALBUM_NAME = "SDCARD"

        const val RECENT_ALBUM_NAME = "Semua Media"
        const val RECENT_ALBUM_ID = -1L
    }

}