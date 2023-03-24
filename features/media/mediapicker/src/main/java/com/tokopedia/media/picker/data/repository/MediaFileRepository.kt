package com.tokopedia.media.picker.data.repository

import com.tokopedia.media.picker.data.MediaQueryDataSource
import com.tokopedia.media.picker.data.MediaQueryDataSourceImpl.Companion.BUCKET_ALL_MEDIA_ID
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.utils.isOppoManufacturer
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

            cursor?.close()
            emit(result)
        }
    }

    companion object {
        private const val LIMIT_MEDIA_OFFSET = 100
    }
}
