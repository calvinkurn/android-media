package com.tokopedia.home_component.usecase.missionwidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.home_component.model.AtfContent
import com.tokopedia.home_component_header.model.ChannelHeader

/**
 * Created by dhaba
 */
class HomeMissionWidgetData {
    data class HomeMissionWidget(
        @SerializedName("getHomeMissionWidget")
        @Expose
        val getHomeMissionWidget: GetHomeMissionWidget = GetHomeMissionWidget(),
    )

    data class GetHomeMissionWidget(
        @SerializedName("header")
        @Expose
        val header: Header = Header(),
        @SerializedName("appLog")
        @Expose
        val appLog: AppLog = AppLog(),
        @SerializedName("missions")
        @Expose
        val missions: List<Mission> = listOf(),
        @SerializedName("config")
        @Expose
        val config: Config = Config(),
    ): AtfContent

    data class Header(
        @SerializedName("title")
        @Expose
        val title: String = "",
    )

    data class Config(
        @SerializedName("styleParam")
        @Expose
        val styleParam: String = "",
        @SerializedName("dividerType")
        @Expose
        val dividerType: Int = 0,
    )

    data class AppLog(
        @SerializedName("bytedanceSessionID")
        @Expose
        val bytedanceSessionId: String = "",
        @SerializedName("requestID")
        @Expose
        val requestId: String = "",
        @SerializedName("logID")
        @Expose
        val logId: String = "",
    )

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
        @SerializedName("parentProductID")
        @Expose
        val parentProductID: String = "",
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
        val shopId: String = "",
        @SerializedName("campaignCode")
        @Expose
        val campaignCode: String = "",
        @SerializedName("recParam")
        @Expose
        val recParam: String = "",
        @SerializedName("labelGroup")
        @Expose
        val labelGroup: List<LabelGroup> = listOf(),
    ) {

        data class LabelGroup(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("position")
            @Expose
            val position: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("styles")
            @Expose
            val styles: List<Styles> = listOf(),
        ) {

            data class Styles(
                @SerializedName("key")
                @Expose
                val key: String = "",

                @SerializedName("value")
                @Expose
                val value: String = "",
            )
        }
    }
}
