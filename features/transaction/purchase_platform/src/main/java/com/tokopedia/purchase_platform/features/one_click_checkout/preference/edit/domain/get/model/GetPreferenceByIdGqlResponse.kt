package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.get.model

import com.google.gson.annotations.SerializedName

data class GetPreferenceByIdGqlResponse(
        @SerializedName("get_profile_by_id_occ")
        val response: GetPreferenceByIdResponse = GetPreferenceByIdResponse()
)