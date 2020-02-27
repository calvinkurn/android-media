package com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.preference

import com.google.gson.annotations.SerializedName

data class PreferenceListGqlResponse (
    @SerializedName("get_preference_list")
    val data: Response
)