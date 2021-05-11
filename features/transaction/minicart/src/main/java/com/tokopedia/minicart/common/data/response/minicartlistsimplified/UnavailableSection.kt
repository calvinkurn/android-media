package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class UnavailableSection(
        @SerializedName("unavailable_group")
        val unavailableGroup: List<UnavailableGroup> = emptyList()
)