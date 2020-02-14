package com.tokopedia.autocomplete.initialstate.newfiles

import com.google.gson.annotations.SerializedName

data class InitialStateData(
        @SerializedName("id")
        var id: String = "",

        @SerializedName("header")
        var header: String = "",

        @SerializedName("label_action")
        var labelAction: String = "",

        @SerializedName("items")
        var items: List<InitialStateItem> = listOf()
) {

    companion object {
        const val INITIAL_STATE_RECENT_VIEW = "recent_view"
        const val INITIAL_STATE_RECENT_SEARCH = "recent_search"
        const val INITIAL_STATE_POPULAR_SEARCH = "popular_search"
    }
}