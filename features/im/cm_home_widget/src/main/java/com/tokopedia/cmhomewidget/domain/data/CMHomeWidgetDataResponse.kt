package com.tokopedia.cmhomewidget.domain.data

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("notifier_getHtdw")
    val cmHomeWidgetDataResponse: CMHomeWidgetDataResponse
)

data class CMHomeWidgetDataResponse(
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val cmHomeWidgetData: CMHomeWidgetData
)

data class CMHomeWidgetData(
    @SerializedName("campaign_id")
    val campaignId: Int,
    @SerializedName("card")
    val card: Card,
    @SerializedName("is_test")
    val isTest: Boolean,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("notification_id")
    val notificationId: Int,
    @SerializedName("parent_id")
    val parentId: Int,
    @SerializedName("products")
    val products: List<Product>,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("widget_title")
    val widgetTitle: String,
    @SerializedName("widget_type")
    val widgetType: String
)

data class Card(
    @SerializedName("app_link")
    val appLink: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("label")
    val label: String
)

data class Product(
    @SerializedName("action_buttons")
    val actionButtons: List<ActionButton>,
    @SerializedName("actual_price")
    val actualPrice: String,
    @SerializedName("app_link")
    val appLink: String,
    @SerializedName("badge_image_url")
    val badgeImageUrl: String,
    @SerializedName("badge_type")
    val badgeType: String,
    @SerializedName("current_price")
    val currentPrice: String,
    @SerializedName("dropped_percent")
    val droppedPercent: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("shop")
    val shop: Shop
)

data class ActionButton(
    @SerializedName("app_link")
    val appLink: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("text")
    val text: String,
    @SerializedName("type")
    val type: String
)

data class Shop(
    @SerializedName("badge_image_url")
    val badgeImageUrl: String,
    @SerializedName("badge_title")
    val badgeTitle: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)