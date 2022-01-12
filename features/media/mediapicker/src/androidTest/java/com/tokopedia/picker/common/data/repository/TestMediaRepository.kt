package com.tokopedia.picker.common.data.repository

import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.ui.PickerParam

class TestMediaRepository : MediaRepository {

    var data = mutableListOf<Media>()

    override suspend fun invoke(
        bucketId: Long,
        param: PickerParam
    ) = data

}