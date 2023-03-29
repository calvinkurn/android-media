package com.tokopedia.media.picker.data.repository

import android.database.Cursor
import com.tokopedia.media.picker.data.MediaQueryDataSource
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl
import com.tokopedia.media.picker.data.entity.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MediaFileRepository {
    operator fun invoke(bucketId: Long, start: Int): Flow<List<Media>>
}

class MediaFileRepositoryImpl @Inject constructor(
    source: MediaQueryDataSource
) : MediaFileRepository, MediaQueryDataSource by source {

    override operator fun invoke(bucketId: Long, start: Int): Flow<List<Media>> {
        val cursor = setupMediaQuery(bucketId, LIMIT_MEDIA_OFFSET)
        val result = mutableListOf<Media>()
        var index = start

        val cursorCount = cursor?.count ?: 0

        val offset = if (cursorCount < LIMIT_MEDIA_OFFSET) {
            cursorCount
        } else {
            LIMIT_MEDIA_OFFSET
        }

        return flow {
            if (bucketId == MediaQueryDataSourceImpl.BUCKET_ALL_MEDIA_ID && start == 0) {
                if (cursor?.moveToFirst() == true) {
                    do {
                        val media = createMedia(cursor) ?: continue
                        if (media.file.exists().not()) continue

                        if (media.file.isVideo()) {
                            media.duration = getVideoDuration(media.file)
                        }

                        result.add(media)

                        if (result.size == offset) break
                    } while (cursor.moveToNext())
                }
            } else {
                while (cursor?.moveToPosition(index) == true) {
                    val media = createMedia(cursor) ?: continue
                    if (media.file.exists().not()) continue

                    if (media.file.isVideo()) {
                        media.duration = getVideoDuration(media.file)
                    }

                    result.add(media)
                    index++

                    if (result.size == offset) break
                }
            }

            cursor?.close()
            emit(result)
        }
    }

    companion object {
        private const val LIMIT_MEDIA_OFFSET = 100
    }
}
