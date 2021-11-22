package com.tokopedia.cmhomewidget.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.cmhomewidget.presentation.adapter.factory.CMHomeWidgetViewHolderTypeFactory
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable

@SuppressLint("Invalid Data Type")
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

@SuppressLint("Invalid Data Type")
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
    val cmHomeWidgetProductCardData: List<CMHomeWidgetProductCardData>?,
    @SerializedName("card")
    val cmHomeWidgetViewAllCardData: CMHomeWidgetViewAllCardData?
)

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetProductCardData(
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
    @SerializedName("actual_price")
    val actualPrice: String?,
    @SerializedName("badge_type")
    val badgeType: String?,
    @SerializedName("badge_image_url")
    val badgeImageUrl: String?,
    @SerializedName("app_link")
    val appLink: String?,
    @SerializedName("shop")
    val cmHomeWidgetShop: CMHomeWidgetShop?,
    @SerializedName("action_buttons")
    val cmHomeWidgetActionButtons: List<CMHomeWidgetActionButton>?
) : CMHomeWidgetVisitable {
    override fun type(typeFactory: CMHomeWidgetViewHolderTypeFactory): Int {
        return typeFactory.type(this)
    }
}

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetShop(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String?,
    @SerializedName("badge_title")
    val badgeTitle: String?,
    @SerializedName("badge_image_url")
    val badgeImageUrl: String?
)

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetActionButton(
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

data class CMHomeWidgetViewAllCardData(
    @SerializedName("label")
    val label: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("app_link")
    val appLink: String?
) : CMHomeWidgetVisitable {
    override fun type(typeFactory: CMHomeWidgetViewHolderTypeFactory): Int {
        return typeFactory.type(this)
    }
}
