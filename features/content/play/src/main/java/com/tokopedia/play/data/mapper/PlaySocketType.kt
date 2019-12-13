package com.tokopedia.play.data.mapper


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PlaySocketType(var value: String) {
    TOTAL_CLICK("TOTAL_CLICK"),
    TOTAL_VIEW("TOTAL_VIEW"),
    CHAT_PEOPLE("MESG"),
    PINNED_MESSAGE("PINNED_MESSAGE"),
    QUICK_REPLY("QUICK_REPLY"),
    EVENT_BANNED("BANNED"),
    EVENT_FREEZE("FREEZE"),
}