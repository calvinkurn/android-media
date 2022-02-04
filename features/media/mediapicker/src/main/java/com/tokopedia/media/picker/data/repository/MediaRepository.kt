package com.tokopedia.media.picker.data.repository

import android.content.Context
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.media.picker.ui.PickerParam

interface MediaRepository {
    suspend operator fun invoke(bucketId: Long, param: PickerParam): List<Media>
}

class MediaRepositoryImpl constructor(
    context: Context
) : LoaderDataSource(context), MediaRepository {

    override suspend fun invoke(bucketId: Long, param: PickerParam): List<Media> {
        val cursor = query(param, bucketId, FIRST_LIMIT)?: error("cannot find the query")
        val medias = mutableListOf<Media>()

        if (cursor.moveToFirst()) {
            do {
                val image = medias(cursor, param)

                if (image != null) {
                    medias.add(image)
                }
            } while (cursor.moveToNext())
        }

        return medias
    }

    companion object {
        private const val FIRST_LIMIT = 1_000
    }

}