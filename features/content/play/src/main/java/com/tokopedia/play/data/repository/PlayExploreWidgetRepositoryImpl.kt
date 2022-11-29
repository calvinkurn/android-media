package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.play.domain.repository.PlayExploreWidgetRepository
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.util.PlayWidgetTools
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/11/22
 */
class PlayExploreWidgetRepositoryImpl @Inject constructor(
    private val playWidgetTools: PlayWidgetTools,
    private val getPlayWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val dispatcher: CoroutineDispatchers) : PlayExploreWidgetRepository{

    override suspend fun getWidgets(
        group: String,
        cursor: String,
        sourceType: String,
        sourceId: String
    ) {
        withContext(dispatcher.io) {
            getPlayWidgetSlotUseCase.executeOnBackground(
                group = group,
                cursor = cursor,
                sourceId = sourceId,
                sourceType = sourceType,
            )
        }
    }

    override suspend fun updateReminder(channelId: String, type: PlayWidgetReminderType) {
        playWidgetTools.updateToggleReminder(channelId = channelId, reminderType = type)
    }
}
