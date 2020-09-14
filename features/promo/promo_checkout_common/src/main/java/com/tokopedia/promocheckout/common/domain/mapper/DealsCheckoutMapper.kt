package com.tokopedia.promocheckout.common.domain.mapper

import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import javax.inject.Inject

open class DealsCheckoutMapper @Inject constructor(){

    fun mapData(data: TravelCollectiveBanner): DataUiModel {
        return DataUiModel(
                success = true
        )
    }
}