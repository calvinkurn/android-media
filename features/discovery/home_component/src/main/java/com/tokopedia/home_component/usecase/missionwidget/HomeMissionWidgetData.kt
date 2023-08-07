package com.tokopedia.home_component.usecase.missionwidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home_component_header.model.ChannelHeader

/**
 * Created by dhaba
 */
class HomeMissionWidgetData {
    data class HomeMissionWidget(
        @SerializedName("getHomeMissionWidget")
        @Expose
        val getHomeMissionWidget: GetHomeMissionWidget = GetHomeMissionWidget()
    )

    data class GetHomeMissionWidget(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),
        @SerializedName("missions")
        @Expose
        val missions: List<Mission> = listOf()
    )

    data class Header(
        @SerializedName("title")
        @Expose
        val title: String = ""
    ) {
        fun getAsHomeComponentHeader() = ChannelHeader(
            name = title
        )
    }

    data class Mission(
        @SerializedName("id")
        @Expose
        val id: Long = 0L,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("subTitle")
        @Expose
        val subTitle: String = "",
        @SerializedName("applink")
        @Expose
        val appLink: String = "",
        @SerializedName("imageURL")
        @Expose
        val imageURL: String = "",
        @SerializedName("pageName")
        @Expose
        val pageName: String = "",
        @SerializedName("categoryID")
        @Expose
        val categoryID: String = "",
        @SerializedName("productID")
        @Expose
        val productID: String = "",
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("recommendationType")
        @Expose
        val recommendationType: String = "",
        @SerializedName("buType")
        @Expose
        val buType: String = "",
        @SerializedName("isTopads")
        @Expose
        val isTopads: Boolean = false,
        @SerializedName("isCarousel")
        @Expose
        val isCarousel: Boolean = false,
        @SerializedName("shopID")
        @Expose
        val shopId: String = ""
    )
}
