package com.tokopedia.cmhomewidget.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.cmhomewidget.presentation.adapter.factory.CMHomeWidgetViewHolderTypeFactory
import com.tokopedia.cmhomewidget.presentation.adapter.visitable.CMHomeWidgetVisitable

@SuppressLint("Invalid Data Type")
data class GetCMHomeWidgetDataGqlResponse(
    @SerializedName("notifier_getHtdw")
    @Expose
    val cmHomeWidgetDataResponse: CMHomeWidgetDataResponse
)

data class CMHomeWidgetDataResponse(
    @SerializedName("status")
    @Expose
    val status: Int,
    @SerializedName("data")
    @Expose
    val cmHomeWidgetData: CMHomeWidgetData?
)

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetData(
    @SerializedName("notification_id")
    @Expose
    val notificationId: String,
    @SerializedName("message_id")
    @Expose
    val messageId: String,
    @SerializedName("is_test")
    @Expose
    val isTest: Boolean,
    @SerializedName("parent_id")
    @Expose
    val parentId: String,
    @SerializedName("campaign_id")
    @Expose
    val campaignId: String,
    @SerializedName("widget_title")
    @Expose
    val widgetTitle: String?,
    @SerializedName("products")
    @Expose
    val cmHomeWidgetProductCardData: List<CMHomeWidgetProductCardData>?,
    @SerializedName("card")
    @Expose
    val cmHomeWidgetViewAllCardData: CMHomeWidgetViewAllCardData?
)

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetProductCardData(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val productName: String?,
    @SerializedName("image_url")
    @Expose
    val productImageUrl: String?,
    @SerializedName("current_price")
    @Expose
    val productCurrentPrice: String?,
    @SerializedName("actual_price")
    @Expose
    val productActualPrice: String?,
    @SerializedName("dropped_percent")
    @Expose
    val productDroppedPercent: String?,
    @SerializedName("badge_image_url")
    @Expose
    val productBadgeImageUrl: String?,
    @SerializedName("app_link")
    @Expose
    val appLink: String?,
    @SerializedName("shop")
    @Expose
    val cmHomeWidgetShop: CMHomeWidgetShop?,
    @SerializedName("action_buttons")
    @Expose
    val cmHomeWidgetActionButtons: List<CMHomeWidgetActionButton>?
) : CMHomeWidgetVisitable {
    override fun type(typeFactory: CMHomeWidgetViewHolderTypeFactory): Int {
        return typeFactory.type(this)
    }
}

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetShop(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("name")
    @Expose
    val shopName: String?,
    @SerializedName("badge_image_url")
    @Expose
    val shopBadgeImageUrl: String?
)

@SuppressLint("Invalid Data Type")
data class CMHomeWidgetActionButton(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("text")
    @Expose
    val actionButtonText: String?,
    @SerializedName("app_link")
    @Expose
    val appLink: String?
)

data class CMHomeWidgetViewAllCardData(
    @SerializedName("label")
    @Expose
    val label: String?,
    @SerializedName("description")
    @Expose
    val description: String?,
    @SerializedName("app_link")
    @Expose
    val appLink: String?
) : CMHomeWidgetVisitable {
    override fun type(typeFactory: CMHomeWidgetViewHolderTypeFactory): Int {
        return typeFactory.type(this)
    }
}
