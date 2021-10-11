package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class UnavailableSection(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("unavailable_description")
        val unavailableDescription: String = "",
        @SerializedName("selected_unavailable_action_id")
        val selectedUnavailableActionId: String = "",
        @SerializedName("action")
        val action: List<Action> = emptyList(),
        @SerializedName("unavailable_group")
        val unavailableGroup: List<UnavailableGroup> = emptyList()
)