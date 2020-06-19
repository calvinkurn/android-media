package com.tokopedia.oneclickcheckout.preference.edit.domain.update.model

import com.google.gson.annotations.SerializedName

data class UpdatePreferenceGqlResponse(
        @SerializedName("update_profile_occ")
        val response: UpdatePreferenceResponse
)