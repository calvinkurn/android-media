package com.tokopedia.people.views.uimodel.saved

/**
 * Created By : Jonathan Darwin on June 30, 2022
 */
sealed interface SavedReminderData {

    data class Saved(
        val channelId: String,
        val position: Int,
        val isActive: Boolean,
    ) : SavedReminderData

    object NoData : SavedReminderData
}
