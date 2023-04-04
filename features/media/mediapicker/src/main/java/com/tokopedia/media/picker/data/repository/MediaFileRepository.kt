package com.tokopedia.media.picker.data.repository

import com.tokopedia.media.picker.data.MediaQueryDataSource
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
        val cursor = setupMediaQuery(bucketId, start, LIMIT_MEDIA_OFFSET)
        val result = mutableListOf<Media>()

        val cursorSize = cursor?.count ?: 0
        var count = 0

        return flow {
            if (cursor?.moveToFirst() == true) {
                do {
                    val media = createMedia(cursor) ?: continue
                    if (media.file.exists().not()) continue

                    if (media.file.isVideo()) {
                        media.duration = getVideoDuration(media.file)
                    }

                    result.add(media)
                    count++

                    if (count == cursorSize) break
                } while (cursor.moveToNext())
            }

            cursor?.close()
            emit(result)
        }
    }

    companion object {
        private const val LIMIT_MEDIA_OFFSET = 100
    }
}
