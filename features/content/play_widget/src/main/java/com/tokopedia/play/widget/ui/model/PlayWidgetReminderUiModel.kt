package com.tokopedia.play.widget.ui.model


/**
 * Created by mzennis on 21/10/20.
 */
data class PlayWidgetReminderUiModel(
        val remind: Boolean = false,
        val success: Boolean,
        val position: Int = -1
)