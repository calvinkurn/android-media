package com.tokopedia.media.picker.common.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.loader.LoaderDataSource

class TestMediaRepository constructor(
    loaderDataSource: LoaderDataSource,
    dispatcher: CoroutineDispatchers
) : MediaRepository(loaderDataSource, dispatcher) {

    var data = mutableListOf<Media>()

    override fun execute(param: Long): List<Media> {
        return data
    }

}
