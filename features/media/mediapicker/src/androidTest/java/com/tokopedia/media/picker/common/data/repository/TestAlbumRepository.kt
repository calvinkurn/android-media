package com.tokopedia.media.picker.common.data.repository

import com.tokopedia.media.picker.data.entity.Album
import com.tokopedia.media.picker.data.repository.AlbumRepository
import com.tokopedia.media.common.PickerParam

class TestAlbumRepository : AlbumRepository {

    var data = mutableListOf<Album>()

    override suspend fun invoke(
        param: PickerParam
    ) = data

}