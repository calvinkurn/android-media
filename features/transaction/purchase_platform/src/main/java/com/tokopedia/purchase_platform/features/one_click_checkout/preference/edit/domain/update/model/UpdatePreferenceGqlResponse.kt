package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceGqlResponse(
        @SerializedName("update_profile_occ")
        val response: UpdatePreferenceResponse
)