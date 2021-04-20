package com.tokopedia.autocomplete.initialstate

import com.google.gson.annotations.SerializedName

data class InitialStateData(
        @SerializedName("id")
        val id: String = "",

        @SerializedName("feature_id")
        val featureId: String = "",

        @SerializedName("header")
        val header: String = "",

        @SerializedName("label_action")
        val labelAction: String = "",

        @SerializedName("items")
        val items: List<InitialStateItem> = listOf()
) {

    companion object {
        const val INITIAL_STATE_RECENT_VIEW = "recent_view"
        const val INITIAL_STATE_RECENT_SEARCH = "recent_search"
        const val INITIAL_STATE_POPULAR_SEARCH = "popular_search"
        const val INITIAL_STATE_CURATED_CAMPAIGN = "curated_campaign"
        const val INITIAL_STATE_LIST_PRODUCT_LINE = "list_product_line"
    }
}