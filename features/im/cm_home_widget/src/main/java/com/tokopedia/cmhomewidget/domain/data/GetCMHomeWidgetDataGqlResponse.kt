package com.tokopedia.cmhomewidget.domain.data

import com.google.gson.annotations.SerializedName

data class GetCMHomeWidgetDataGqlResponse(
    @SerializedName("notifier_getHtdw")
    val cmHomeWidgetDataResponse: CMHomeWidgetDataResponse
)

data class CMHomeWidgetDataResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val cmHomeWidgetData: CMHomeWidgetData?
)

data class CMHomeWidgetData(
    @SerializedName("parent_id")
    val parentId: Long,
    @SerializedName("campaign_id")
    val campaignId: Long,
    @SerializedName("widget_title")
    val widgetTitle: String?,
    @SerializedName("widget_type")
    val widgetType: String?,
    @SerializedName("products")
    val products: List<Product>?,
    @SerializedName("card")
    val card: Card?
)

data class Product(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String?,
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("current_price")
    val currentPrice: String?,
    @SerializedName("dropped_percent")
    val droppedPercent: String?,
    @SerializedName("badge_type")
    val badgeType: String?,
    @SerializedName("badge_image_url")
    val badgeImageUrl: String?,
    @SerializedName("app_link")
    val appLink: String?,
    @SerializedName("shop")
    val shop: Shop?,
    @SerializedName("action_buttons")
    val actionButtons: List<ActionButton>?
)

data class Shop(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String?,
    @SerializedName("badge_title")
    val badgeTitle: String?,
    @SerializedName("badge_image_url")
    val badgeImageUrl: String?
)

data class ActionButton(
    @SerializedName("id")
    val id: Long,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("app_link")
    val appLink: String?
)

data class Card(
    @SerializedName("label")
    val label: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("app_link")
    val appLink: String?
)
