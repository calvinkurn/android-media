package com.tokopedia.play.data.mapper


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PlaySocketType(var value: String) {
    VIDEO("video"),
    VIDEO_STREAM("video_stream"),
    TOTAL_CLICK("channel_lope_lope"),
    TOTAL_VIEW("total_view"),
    CHAT_ADMIN("admm"),
    CHAT_GENERATED_MESSAGE("generated_msg"),
    CHAT_PEOPLE("mesg"),
    PINNED_MESSAGE("pinned_message"),
    QUICK_REPLY("quick_reply"),
    EVENT_BANNED("banned"),
    EVENT_FREEZE("freeze")
}