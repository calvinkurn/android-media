package com.tokopedia.play.data.mapper


/**
 * Created by mzennis on 2019-12-12.
 */
enum class PlaySocketType(var value: String) {
    TotalClick("TOTAL_CLICK"),
    TotalView("TOTAL_VIEW"),
    ChatPeople("MESG"),
    PinnedMessage("PINNED_MESSAGE"),
    QuickReply("QUICK_REPLY"),
    EventBanned("BANNED"),
    EventFreeze("FREEZE")
}