package com.tokopedia.play_common.domain.model

import com.google.gson.annotations.SerializedName

data class PlayGetWidgetEntity (
    @SerializedName("playGetWidgetV2") val playGetWidgetV2 : PlayWidget
)

data class PlayWidget (
        @SerializedName("data") val data : List<PlayWidgetItem>,
        @SerializedName("meta") val meta : Meta
)

data class Meta (
    @SerializedName("isAutoRefresh") val isAutoRefresh : Boolean = false,
    @SerializedName("autoRefreshTimer") val autoRefreshTimer : Int,
    @SerializedName("widgetTitle") val widgetTitle : String,
    @SerializedName("buttonText") val buttonText : String,
    @SerializedName("widgetBackground") val widgetBackground : String,
    @SerializedName("autoplayAmount") val autoplayAmount : Int,
    @SerializedName("autoplay") val autoplay : Boolean,
    @SerializedName("buttonApplink") val buttonApplink : String,
    @SerializedName("buttonWeblink") val buttonWeblink : String,
    @SerializedName("overlayImage") val overlayImage : String,
    @SerializedName("overlayImageApplink") val overlayImageApplink : String = "",
    @SerializedName("overlayImageWeblink") val overlayImageWeblink : String = "",
    @SerializedName("gradient") val gradient : List<String> = listOf(),
    @SerializedName("serverTimeOffset") val serverTimeOffset : Long
)