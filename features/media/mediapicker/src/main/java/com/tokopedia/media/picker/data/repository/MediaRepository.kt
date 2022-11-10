package com.tokopedia.media.picker.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.loader.LoaderDataSource
import com.tokopedia.picker.common.base.BaseRepository

open class MediaRepository constructor(
    loaderDataSource: LoaderDataSource,
    dispatchers: CoroutineDispatchers
) : BaseRepository<Long, List<Media>>(dispatchers.io)
    , LoaderDataSource by loaderDataSource {

    override fun execute(param: Long): List<Media> {
        val cursor = query(param, FIRST_LIMIT)?: return emptyList()
        val medias = mutableListOf<Media>()

        if (cursor.moveToFirst()) {
            do {
                val image = medias(cursor)?: continue
                if (image.file.exists().not()) continue

                medias.add(image)
            } while (cursor.moveToNext())
        }

        return medias
    }

    companion object {
        private const val FIRST_LIMIT = 1_000
    }

}
