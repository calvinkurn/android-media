package com.tokopedia.picker.fake.data.repository

import com.tokopedia.picker.data.entity.Album
import com.tokopedia.picker.data.repository.AlbumRepository
import com.tokopedia.picker.ui.PickerParam

class TestAlbumRepository : AlbumRepository {

    override suspend fun invoke(param: PickerParam): List<Album> {
        return emptyList()
    }

}