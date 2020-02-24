package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.Response
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.PreferenceListResponseModel

interface PreferenceDataMapper {

    fun convertToDomainModel(response: Response) : PreferenceListResponseModel
}