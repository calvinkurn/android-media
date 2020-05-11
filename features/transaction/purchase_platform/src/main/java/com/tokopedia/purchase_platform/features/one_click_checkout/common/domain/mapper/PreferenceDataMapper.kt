package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper

import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference.PreferenceListGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.PreferenceListResponseModel

interface PreferenceDataMapper {

    fun convertToDomainModel(response: PreferenceListGqlResponse) : PreferenceListResponseModel
}