package com.tokopedia.media.picker.common.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.loader.LoaderDataSource

class TestAlbumRepository constructor(
    loaderDataSource: LoaderDataSource,
    dispatcher: CoroutineDispatchers
) : AlbumRepository(loaderDataSource, dispatcher) {

    var data = mutableListOf<Album>()

    override fun execute(param: Unit): List<Album> {
        return data
    }

}
