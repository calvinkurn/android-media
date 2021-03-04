package com.tokopedia.play.widget.ui.model


/**
 * Created by mzennis on 21/10/20.
 */
enum class PlayWidgetActionReminder {
    Remind,
    UnRemind,
}

fun PlayWidgetActionReminder.revert(): PlayWidgetActionReminder {
    return if (this == PlayWidgetActionReminder.Remind) PlayWidgetActionReminder.UnRemind else PlayWidgetActionReminder.Remind
}

sealed class PlayWidgetReminderEvent {
    abstract val channelId: String
    abstract val actionReminder: PlayWidgetActionReminder

    data class NeedLoggedIn(
            override val channelId: String,
            override val actionReminder: PlayWidgetActionReminder
    ) : PlayWidgetReminderEvent()
}