package com.tokopedia.media.picker.data.repository

import com.tokopedia.media.picker.data.MediaQueryDataSource
import com.tokopedia.media.picker.data.entity.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface MediaFileRepository {
    operator fun invoke(bucketId: Long, start: Int): Flow<List<Media>>
    fun maxLimitSize(): Int
}

class MediaFileRepositoryImpl @Inject constructor(
    source: MediaQueryDataSource
) : MediaFileRepository, MediaQueryDataSource by source {

    override fun maxLimitSize(): Int {
        return LIMIT_MEDIA_SIZE
    }

    override operator fun invoke(bucketId: Long, start: Int): Flow<List<Media>> {
        return flow {
            try {
                val cursor = setupMediaQuery(bucketId, start, maxLimitSize())
                val result = mutableListOf<Media>()

                if (cursor?.moveToFirst() == true) {
                    do {
                        val media = createMedia(cursor) ?: continue
                        if (media.file.exists().not()) continue

                        if (media.file.isVideo()) {
                            media.duration = getVideoDuration(media.file)
                        }

                        result.add(media)
                    } while (cursor.moveToNext())
                }

                emit(result)
                cursor?.close()
            } catch (t: Throwable) {
                emit(emptyList())
            }
        }
    }

    companion object {
        const val LIMIT_MEDIA_SIZE = 100
    }
}
