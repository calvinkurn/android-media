package com.tokopedia.media.picker.common.data.repository

import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.data.repository.MediaRepository
import com.tokopedia.media.picker.ui.PickerParam

class TestMediaRepository : MediaRepository {

    var data = mutableListOf<Media>()

    override suspend fun invoke(
        bucketId: Long,
        param: PickerParam
    ) = data

}