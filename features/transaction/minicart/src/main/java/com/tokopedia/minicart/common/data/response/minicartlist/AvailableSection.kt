package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class AvailableSection(
        @SerializedName("action")
        val action: List<Action> = emptyList(),
        @SerializedName("available_group")
        val availableGroup: List<AvailableGroup> = emptyList()
)