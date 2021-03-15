package com.tokopedia.play.widget.ui.model


/**
 * Created by mzennis on 21/10/20.
 */
enum class PlayWidgetReminderType {
    Remind,
    UnRemind;
}

val PlayWidgetReminderType.reminded: Boolean
    get() = this == PlayWidgetReminderType.Remind

fun PlayWidgetReminderType.switch(): PlayWidgetReminderType {
    return if (this == PlayWidgetReminderType.Remind) PlayWidgetReminderType.UnRemind else PlayWidgetReminderType.Remind
}

fun getReminderType(remind: Boolean): PlayWidgetReminderType = if (remind) PlayWidgetReminderType.Remind else PlayWidgetReminderType.UnRemind