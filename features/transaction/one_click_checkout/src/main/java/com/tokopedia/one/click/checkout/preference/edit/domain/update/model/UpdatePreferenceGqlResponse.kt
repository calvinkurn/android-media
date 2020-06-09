package com.tokopedia.one.click.checkout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceGqlResponse(
        @SerializedName("update_profile_occ")
        val response: UpdatePreferenceResponse
)