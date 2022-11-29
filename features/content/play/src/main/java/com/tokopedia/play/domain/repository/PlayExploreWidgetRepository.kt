package com.tokopedia.play.domain.repository

import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 29/11/22
 */
interface PlayExploreWidgetRepository {
    /**
     * Setup return data
     */

    suspend fun getWidgets(
        group: String,
        cursor: String = "",
        sourceType: String,
        sourceId: String //channelId == sourceId
    )

    suspend fun updateReminder(channelId: String, type: PlayWidgetReminderType)
}
