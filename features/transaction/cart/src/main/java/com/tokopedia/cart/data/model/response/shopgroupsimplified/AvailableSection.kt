package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class AvailableSection(
        @SerializedName("action")
        val actions: List<Action> = emptyList(),
        @SerializedName("available_group")
        val availableGroupGroups: List<AvailableGroup> = emptyList()
)