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
        val cursor = setupMediaQuery(bucketId, LIMIT_MEDIA_OFFSET)
        val result = mutableListOf<Media>()
        var index = start

        return flow {
            while (cursor?.moveToPosition(index) == true) {
                val media = createMedia(cursor)?: continue
                if (media.file.exists().not()) continue

                if (media.file.isVideo()) {
                    media.videoLength = getVideoDuration(media.file)
                }

                result.add(media)
                index++

                if (result.size == LIMIT_MEDIA_OFFSET) break
            }

            cursor?.close()

            emit(result)
        }
    }

    companion object {
        private const val LIMIT_MEDIA_OFFSET = 100
    }
}
