package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class AvailableSection(
        @SerializedName("available_group")
        val availableGroup: List<AvailableGroup> = emptyList()
)