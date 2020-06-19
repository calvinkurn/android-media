package com.tokopedia.oneclickcheckout.preference.list.domain.model

import com.google.gson.annotations.SerializedName

data class SetDefaultPreferenceGqlResponse(
        @SerializedName("set_default_profile_occ")
        val response: SetDefaultPreferenceResponse = SetDefaultPreferenceResponse()
)