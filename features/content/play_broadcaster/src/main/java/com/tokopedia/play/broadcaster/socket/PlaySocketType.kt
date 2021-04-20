package com.tokopedia.play.broadcaster.socket


/**
 * Created by mzennis on 22/06/20.
 */
interface PlaySocketType {
    val type: PlaySocketEnum
}

enum class PlaySocketEnum(var value: String) {
    TotalView("TOTAL_VIEW"),
    TotalLike("TOTAL_LIKE"),
    NewMetric("GENERAL_BULK_EVENT_NOTIF"),
    LiveStats("REPORT_CHANNEL"),
    LiveDuration("LIVE_DURATION"),
    ProductTag("PRODUCT_TAG"),
    Banned("MODERATE"),
    Chat("MESG"),
    Freeze("FREEZE")
}