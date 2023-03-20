package com.tokopedia.people.views.uimodel.saved

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created By : Jonathan Darwin on June 30, 2022
 */
sealed interface SavedReminderData {

    data class Saved(val channel: PlayWidgetChannelUiModel) : SavedReminderData

    object NoData : SavedReminderData
}
