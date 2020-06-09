package com.tokopedia.one.click.checkout.common.domain.mapper

import com.tokopedia.one.click.checkout.common.data.model.response.preference.PreferenceListGqlResponse
import com.tokopedia.one.click.checkout.common.domain.model.preference.PreferenceListResponseModel

interface PreferenceDataMapper {

    fun convertToDomainModel(response: PreferenceListGqlResponse) : PreferenceListResponseModel
}