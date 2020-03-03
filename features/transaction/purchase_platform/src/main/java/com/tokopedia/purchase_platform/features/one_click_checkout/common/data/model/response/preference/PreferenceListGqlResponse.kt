package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class PreferenceListGqlResponse (
    @SerializedName("get_all_profiles_occ")
    val data: PreferenceListResponse = PreferenceListResponse()
)