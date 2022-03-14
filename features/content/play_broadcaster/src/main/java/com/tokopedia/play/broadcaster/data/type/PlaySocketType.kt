package com.tokopedia.play.broadcaster.data.type


/**
 * Created by mzennis on 22/06/20.
 */
interface PlaySocketType {
    val type: PlaySocketEnum
}

enum class PlaySocketEnum(val value: String) {
    TotalView("TOTAL_VIEW"),
    TotalLike("TOTAL_LIKE"),
    NewMetric("GENERAL_BULK_EVENT_NOTIF"),
    LiveStats("REPORT_CHANNEL"),
    LiveDuration("LIVE_DURATION"),
    ProductTag("PRODUCT_TAG_UPDATE"),
    Banned("MODERATE"),
    Chat("MESG"),
    Freeze("FREEZE"),
    ChannelInteractive("CHANNEL_INTERACTIVE"),
    PinnedMessage("PINNED_MESSAGE"),
}