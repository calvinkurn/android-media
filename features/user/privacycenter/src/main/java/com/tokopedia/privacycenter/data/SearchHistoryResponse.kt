package com.tokopedia.privacycenter.data

import com.google.gson.annotations.SerializedName

data class SearchHistoryResponse(
    @SerializedName("universe_initial_state")
    val universeInitialState: UniverseInitialState = UniverseInitialState()
)

data class ItemSearch(
    @SerializedName("applink")
    val applink: String = "",

    @SerializedName("icon_subtitle")
    val iconSubtitle: String = "",

    @SerializedName("icon_title")
    val iconTitle: String = "",

    @SerializedName("image_url")
    val imageUrl: String = "",

    @SerializedName("subtitle")
    val subtitle: String = "",

    @SerializedName("label_type")
    val labelType: String = "",

    @SerializedName("id")
    val id: String = "",

    @SerializedName("label")
    val label: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("shortcut_image")
    val shortcutImage: String = ""
)

data class DataItem(
    @SerializedName("feature_id")
    val featureId: String = "",

    @SerializedName("label_action")
    val labelAction: String = "",

    @SerializedName("header")
    val header: String = "",

    @SerializedName("id")
    val id: String = "",

    @SerializedName("items")
    val items: List<ItemSearch> = emptyList()
)

data class UniverseInitialState(
    @SerializedName("data")
    val data: List<DataItem> = emptyList()
)
