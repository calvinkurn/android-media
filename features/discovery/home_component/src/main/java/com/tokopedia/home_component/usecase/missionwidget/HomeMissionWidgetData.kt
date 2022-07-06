package com.tokopedia.home_component.usecase.missionwidget

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        @SerializedName("missions")
        @Expose
        val missions: List<Mission> = listOf()
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
        val imageURL: String = ""
    )
}