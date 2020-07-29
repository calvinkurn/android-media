package com.tokopedia.play.broadcaster.socket


/**
 * Created by mzennis on 22/06/20.
 */
interface PlaySocketType {
    val type: PlaySocketEnum
}

enum class PlaySocketEnum(var value: String) {
    ConcurrentUser("CONCURRENT_USER"),
    Metric("BULK_EVENT_NOTIF"),
    LiveStats("REPORT_CHANNEL"),
    LiveDuration("LIVE_DURATION")
}