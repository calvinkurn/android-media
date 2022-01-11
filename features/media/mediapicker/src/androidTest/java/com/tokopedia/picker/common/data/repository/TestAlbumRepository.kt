package com.tokopedia.picker.common.data.repository

import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.repository.AlbumRepository
import com.tokopedia.picker.ui.PickerParam

class TestAlbumRepository : AlbumRepository {

    var data = mutableListOf<Album>()

    override suspend fun invoke(
        param: PickerParam
    ) = data

}