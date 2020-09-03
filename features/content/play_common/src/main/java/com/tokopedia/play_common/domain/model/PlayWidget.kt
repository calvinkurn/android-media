package com.tokopedia.play_common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlayGetWidgetEntity (
        @SerializedName("playGetWidgetV2") @Expose val playGetWidgetV2 : PlayWidget = PlayWidget()
)

data class PlayWidget (
        @SerializedName("data") @Expose val data : List<PlayWidgetItem> = listOf(),
        @SerializedName("meta") @Expose val meta : Meta = Meta()
)

data class Meta (
        @SerializedName("isAutoRefresh") @Expose val isAutoRefresh : Boolean = false,
        @SerializedName("autoRefreshTimer") @Expose val autoRefreshTimer : Int = 0,
        @SerializedName("widgetTitle") @Expose val widgetTitle : String = "",
        @SerializedName("buttonText") @Expose val buttonText : String = "",
        @SerializedName("widgetBackground") @Expose val widgetBackground : String = "",
        @SerializedName("autoplayAmount") @Expose val autoplayAmount : Int = 0,
        @SerializedName("autoplay") @Expose val autoplay : Boolean = false,
        @SerializedName("buttonApplink") @Expose val buttonApplink : String = "",
        @SerializedName("buttonWeblink") @Expose val buttonWeblink : String = "",
        @SerializedName("overlayImage") @Expose val overlayImage : String = "",
        @SerializedName("overlayImageAppLink") @Expose val overlayImageApplink : String = "",
        @SerializedName("overlayImageWebLink") @Expose val overlayImageWeblink : String = "",
        @SerializedName("gradient") @Expose val gradient : List<String> = listOf(),
        @SerializedName("maxAutoplayCell") @Expose val maxAutoplayCell : Int = 0
)