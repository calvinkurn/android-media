package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class UnavailableSection(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("unavailable_description")
    val unavailableDescription: String = "",
    @SerializedName("action")
    val actions: List<Action> = emptyList(),
    @SerializedName("unavailable_group")
    val unavailableGroups: List<UnavailableGroup> = emptyList(),
    @SerializedName("selected_unavailable_action_id")
    val selectedUnavailableActionId: String = "",
    @SerializedName("products_count")
    val productsCount: Int = 0
)
