package com.tokopedia.play_common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

const val TYPE_BANNER = "PlayWidgetBanner"
const val TYPE_CHANNEL = "PlayWidgetChannel"

data class BufferControl (
        @SerializedName("maxBufferInSecond")
        @Expose
        val maxBufferInSecond: Int = -1,
        @SerializedName("minBufferInSecond")
        @Expose
        val minBufferInSecond: Int = -1,
        @SerializedName("bufferForPlayback")
        @Expose
        val bufferForPlayback: Int = -1,
        @SerializedName("bufferForPlaybackAfterRebuffer")
        @Expose
        val bufferForPlaybackAfterRebuffer: Int = -1
)

data class Config (
        @SerializedName("hasPromo")
        @Expose
        val hasPromo: Boolean = false,
        @SerializedName("isReminderSet")
        @Expose
        val isReminderSet: Boolean = false
)

data class PlayWidgetItem (
        @SerializedName("__typename")
        @Expose
        val typename: String = "",
        @SerializedName("ID")
        @Expose
        val id: String = "",
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("startTime")
        @Expose
        val startTime: String = "",
        @SerializedName("widgetType")
        @Expose
        val widgetType: String = "",
        @SerializedName("appLink")
        @Expose
        val appLink: String = "",
        @SerializedName("webLink")
        @Expose
        val webLink: String = "",
        @SerializedName("config")
        @Expose
        val config: Config = Config(),
        @SerializedName("partner")
        @Expose
        val partner: Partner = Partner(),
        @SerializedName("video")
        @Expose
        val video: Video = Video(),
        @SerializedName("stats")
        @Expose
        val stats: Stats = Stats(),
        @SerializedName("backgroundURL")
        val backgroundURL : String = "",
        @SerializedName("buttonColor")
        val buttonColor : String = "",
        @SerializedName("buttonText")
        val buttonText : String = "",
        @SerializedName("buttonAppLink")
        val buttonAppLink : String = "",
        @SerializedName("buttonWebLink")
        val buttonWebLink : String = ""
){
    fun isBanner() = typename == TYPE_BANNER
    fun isChannel() = typename == TYPE_CHANNEL
}

data class Partner (
        @SerializedName("ID")
        @Expose
        val id: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("name")
        @Expose
        val name: String = ""
)

data class Stats (
        @SerializedName("view")
        @Expose
        val view: View = View()
)


data class Video (
        @SerializedName("ID")
        @Expose
        val id: String = "",
        @SerializedName("orientation")
        @Expose
        val orientation: String = "",
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("coverUrl")
        @Expose
        val coverUrl: String = "",
        @SerializedName("streamSource")
        @Expose
        val streamSource: String = "",
        @SerializedName("isAutoplay")
        @Expose
        val isAutoplay: Boolean = false,
        @SerializedName("isShowTotalView")
        @Expose
        val isShowTotalView: Boolean = false,
        @SerializedName("isLive")
        @Expose
        val isLive: Boolean = false,
        @SerializedName("bufferControl")
        @Expose
        val bufferControl: BufferControl = BufferControl()
)

data class View(
        @SerializedName("value")
        @Expose
        val value: Int = -1,
        @SerializedName("formatted")
        @Expose
        val formatted: String = "",
        @SerializedName("visible")
        @Expose
        val visible: Boolean = false
)