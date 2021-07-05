package com.tokopedia.play.widget.ui.model


/**
 * Created by mzennis on 21/10/20.
 */
enum class PlayWidgetReminderType {
    Reminded,
    NotReminded;
}

val PlayWidgetReminderType.reminded: Boolean
    get() = this == PlayWidgetReminderType.Reminded

fun PlayWidgetReminderType.switch(): PlayWidgetReminderType {
    return if (this == PlayWidgetReminderType.Reminded) PlayWidgetReminderType.NotReminded else PlayWidgetReminderType.Reminded
}

fun getReminderType(remind: Boolean): PlayWidgetReminderType = if (remind) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded