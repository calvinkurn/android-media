package com.tokopedia.play.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.usecase.GetPlayWidgetSlotUseCase
import com.tokopedia.play.domain.repository.PlayExploreWidgetRepository
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.view.uimodel.mapper.PlayExploreWidgetMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.util.PlayWidgetConnectionUtil
import com.tokopedia.play.widget.util.PlayWidgetTools
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by astidhiyaa on 29/11/22
 */
class PlayExploreWidgetRepositoryImpl @Inject constructor(
    private val playWidgetTools: PlayWidgetTools,
    private val getPlayWidgetSlotUseCase: GetPlayWidgetSlotUseCase,
    private val mapper: PlayExploreWidgetMapper,
    private val reminderMapper: PlayWidgetUiMapper,
    private val connectionUtil: PlayWidgetConnectionUtil,
    private val dispatcher: CoroutineDispatchers
) : PlayExploreWidgetRepository {

    override suspend fun getWidgets(
        group: String,
        cursor: String,
        sourceType: String,
        sourceId: String
    ): List<WidgetUiModel> = withContext(dispatcher.io) {
        mapper.map(
            getPlayWidgetSlotUseCase.executeOnBackground(
                group = group,
                cursor = cursor,
                sourceId = sourceId,
                sourceType = sourceType,
                isWifi = connectionUtil.isEligibleForHeavyDataUsage(),
            )
        )
    }

    override suspend fun updateReminder(
        channelId: String,
        type: PlayWidgetReminderType
    ): Boolean {
        val result = playWidgetTools.updateToggleReminder(
            channelId = channelId,
            reminderType = type,
            coroutineContext = dispatcher.io
        )
        return reminderMapper.mapWidgetToggleReminder(result)
    }
}
