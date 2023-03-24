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

        return flow {
            while (cursor?.moveToPosition(index) == true) {
                val media = createMedia(cursor) ?: continue
                if (media.file.exists().not()) continue

                if (media.file.isVideo()) {
                    media.duration = getVideoDuration(media.file)
                }

                result.add(media)
                index++

                if (result.size == LIMIT_MEDIA_OFFSET) break

                /**
                 * if the device only contains under 100 item of medias in all-media's bucket id.
                 * This validation needs to fix infinite loop on OPPO and VIVO devices.
                 */
                if (cursor.count < LIMIT_MEDIA_OFFSET
                    && cursor.isAfterLast.not()
                    && bucketId == BUCKET_ALL_MEDIA_ID
                    && isOppoManufacturer()
                ) break
            }

            cursor?.close()
            emit(result)
        }
    }

    companion object {
        private const val LIMIT_MEDIA_OFFSET = 100
    }
}
