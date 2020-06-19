package com.tokopedia.oneclickcheckout.common.domain.mapper

import com.tokopedia.oneclickcheckout.common.data.model.response.preference.PreferenceListGqlResponse
import com.tokopedia.oneclickcheckout.common.domain.model.preference.PreferenceListResponseModel

interface PreferenceDataMapper {

    fun convertToDomainModel(response: PreferenceListGqlResponse) : PreferenceListResponseModel
}