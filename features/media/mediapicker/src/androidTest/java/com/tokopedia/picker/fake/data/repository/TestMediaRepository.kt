package com.tokopedia.picker.fake.data.repository

import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.data.repository.MediaRepository
import com.tokopedia.picker.ui.PickerParam

class TestMediaRepository : MediaRepository {

    override suspend fun invoke(bucketId: Long, param: PickerParam): List<Media> {
        return listOf(
            Media(1, "Isfa", ""),
            Media(2, "Isfa", ""),
            Media(3, "Isfa", ""),
        )
    }

}