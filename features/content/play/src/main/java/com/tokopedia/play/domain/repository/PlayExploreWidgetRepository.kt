package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

/**
 * @author by astidhiyaa on 29/11/22
 */
interface PlayExploreWidgetRepository {
    suspend fun getWidgets(
        group: String,
        cursor: String = "",
        sourceType: String,
        sourceId: String //channelId == sourceId
    ) : List<WidgetUiModel>

    suspend fun updateReminder(channelId: String, type: PlayWidgetReminderType) : Boolean
}
