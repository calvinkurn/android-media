package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.domain.model

import com.google.gson.annotations.SerializedName

data class DeletePreferenceGqlResponse(
        @SerializedName("delete_profile_occ")
        val data: DeletePreferenceResponse = DeletePreferenceResponse()
)