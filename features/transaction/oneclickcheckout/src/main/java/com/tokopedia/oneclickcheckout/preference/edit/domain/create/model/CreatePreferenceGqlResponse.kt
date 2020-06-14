package com.tokopedia.oneclickcheckout.preference.edit.domain.create.model

import com.google.gson.annotations.SerializedName

data class CreatePreferenceGqlResponse(
        @SerializedName("insert_profile_occ")
        val response: CreatePreferenceResponse
)