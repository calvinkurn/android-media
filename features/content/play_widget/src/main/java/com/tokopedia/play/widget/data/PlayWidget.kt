package com.tokopedia.play.widget.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidget(
        @SerializedName("data") val data: List<PlayWidgetItem> = emptyList(),
        @SerializedName("meta") val meta: PlayWidgetMeta = PlayWidgetMeta()
)

data class PlayWidgetResponse(
        @SerializedName("playGetWidgetV2") val playWidget: PlayWidget = PlayWidget()
)

data class PlayWidgetItem(
        @SerializedName("__typename") val typename: String = "",
        @SerializedName("ID") val id: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("widgetType") val widgetType: String = "",
        @SerializedName("appLink") val appLink: String = "",
        @SerializedName("webLink") val webLink: String = "",
        @SerializedName("startTime") val startTime: String = "",
        @SerializedName("config") val config: PlayWidgetItemConfig = PlayWidgetItemConfig(),
        @SerializedName("partner") val partner: PlayWidgetItemPartner = PlayWidgetItemPartner(),
        @SerializedName("video") val video: PlayWidgetItemVideo = PlayWidgetItemVideo(),
        @SerializedName("stats") val stats: PlayWidgetItemStat = PlayWidgetItemStat(),
        @SerializedName("share") val share: PlayWidgetItemShare = PlayWidgetItemShare(),
        @SerializedName("backgroundURL") val backgroundUrl : String = "",
        @SerializedName("performanceSummaryPageLink") val performanceSummaryPageLink : String = ""
)

data class PlayWidgetItemConfig(
        @SerializedName("hasPromo") val hasPromo: Boolean = false,
        @SerializedName("isReminderSet") val isReminderSet: Boolean = false
)

data class PlayWidgetItemPartner(
        @SerializedName("ID") val id: String = "",
        @SerializedName("name") val name: String = ""
)

data class PlayWidgetItemStat(
        @SerializedName("view") val view: PlayWidgetItemStatView = PlayWidgetItemStatView()
)

data class PlayWidgetItemShare(
        @SerializedName("text") val text: String = "",
        @SerializedName("redirect_url") val redirectUrl: String = "",
        @SerializedName("use_short_url") val useShortUrl: Boolean = false,
        @SerializedName("meta_title") val metaTitle: String = "",
        @SerializedName("meta_description") val metaDescription: String = "",
        @SerializedName("is_show_button") val isShowButton: Boolean = false
)

data class PlayWidgetItemStatView(
        @SerializedName("formatted") val formatted: String = ""
)

data class PlayWidgetItemVideo(
        @SerializedName("ID") val id: String = "",
        @SerializedName("type") val type: String = "",
        @SerializedName("coverUrl") val coverUrl: String = "",
        @SerializedName("streamSource") val streamSource: String = "",
        @SerializedName("isShowTotalView") val isShowTotalView: Boolean = false,
        @SerializedName("isLive") val isLive: Boolean = false
)

data class PlayWidgetMeta(
    @SerializedName("widgetTitle") val widgetTitle: String = "",
    @SerializedName("buttonText") val buttonText: String = "",
    @SerializedName("buttonApplink") val buttonApplink: String = "",
    @SerializedName("buttonWeblink") val buttonWeblink: String = "",
    @SerializedName("widgetBackground") val widgetBackground: String = "",
    @SerializedName("overlayImage") val overlayImage: String = "",
    @SerializedName("overlayImageAppLink") val overlayImageAppLink: String = "",
    @SerializedName("overlayImageWebLink") val overlayImageWebLink: String = "",
    @SerializedName("gradient") val gradient: List<String> = emptyList(),
    @SerializedName("isAutoRefresh") val autoRefresh: Boolean = false,
    @SerializedName("autoRefreshTimer") val autoRefreshTimer: Long = 0,
    @SerializedName("autoplayAmount") val autoplayAmount: Int = 0,
    @SerializedName("autoplay") val autoplay: Boolean = false,
    @SerializedName("maxAutoplayCell") val maxAutoplayCell: Int = 0,
    @SerializedName("maxAutoplayWifi") val maxAutoplayWifi: Int = 0,
    @SerializedName("template") val template: String = "",
    @SerializedName("isButtonVisible") val isButtonVisible: Boolean = true,
    @SerializedName("businessWidgetPosition") val businessWidgetPosition: Int = 0
)