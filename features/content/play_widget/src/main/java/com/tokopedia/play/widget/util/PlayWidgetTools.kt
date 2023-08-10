package com.tokopedia.play.widget.util

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.mapper.PlayWidgetUiMapper
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.types.PlayChannelStatusType
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 08/10/20
 */
class PlayWidgetTools @Inject constructor(
    private val useCase: PlayWidgetUseCase,
    private val lazyReminderUseCase: Lazy<PlayWidgetReminderUseCase>,
    private val lazyUpdateChannelUseCase: Lazy<PlayWidgetUpdateChannelUseCase>,
    private val mapper: PlayWidgetUiMapper,
    private val connectionUtil: PlayWidgetConnectionUtil,
) {

    private val reminderUseCase: PlayWidgetReminderUseCase
        get() = lazyReminderUseCase.get()

    private val updateChannelUseCase: PlayWidgetUpdateChannelUseCase
        get() = lazyUpdateChannelUseCase.get()

    suspend fun getWidgetFromNetwork(
            widgetType: PlayWidgetUseCase.WidgetType,
            coroutineContext: CoroutineContext = Dispatchers.IO): PlayWidget {
        return withContext(coroutineContext) {
            useCase.setQuery(widgetType, connectionUtil.isEligibleForHeavyDataUsage())
            useCase.executeOnBackground()
        }
    }

    suspend fun mapWidgetToModel(
        widgetResponse: PlayWidget,
        prevState: PlayWidgetState? = null,
        coroutineContext: CoroutineContext = Dispatchers.Default,
        extraInfo: PlayWidgetUiMapper.ExtraInfo = PlayWidgetUiMapper.ExtraInfo(),
    ): PlayWidgetState {
        return withContext(coroutineContext) {
            val model = mapper.mapWidget(widgetResponse, prevState, extraInfo)
            PlayWidgetState(
                model = model,
                widgetType = PlayWidgetType.getByTypeString(widgetResponse.meta.template),
                isLoading = false,
            )
        }
    }

    suspend fun updateToggleReminder(channelId: String,
                                     reminderType: PlayWidgetReminderType,
                                     coroutineContext: CoroutineContext = Dispatchers.IO): PlayWidgetReminder {
        return withContext(coroutineContext) {
            reminderUseCase.setRequestParams(PlayWidgetReminderUseCase.createParams(channelId, reminderType.reminded))
            reminderUseCase.executeOnBackground()
        }
    }

    suspend fun mapWidgetToggleReminder(response: PlayWidgetReminder, coroutineContext: CoroutineContext = Dispatchers.Default): Boolean {
        return withContext(coroutineContext) {
            mapper.mapWidgetToggleReminder(response)
        }
    }

    suspend fun deleteChannel(
            channelId: String,
            authorId: String,
            coroutineContext: CoroutineContext = Dispatchers.IO
    ): String {
        return withContext(coroutineContext) {
            updateChannelUseCase.setQueryParams(channelId, authorId, PlayChannelStatusType.Deleted)
            updateChannelUseCase.executeOnBackground()
        }
    }

    fun updateTotalView(state: PlayWidgetState, channelId: String, totalView: String): PlayWidgetState {
        return state.copy(
            model = updateWidgetTotalView(state.model, channelId, totalView)
        )
    }

    fun updateDeletingChannel(state: PlayWidgetState, channelId: String): PlayWidgetState {
        return state.copy(
            model = deletingChannelWidget(state.model, channelId)
        )
    }

    fun updateFailedDeletingChannel(state: PlayWidgetState, channelId: String): PlayWidgetState {
        return state.copy(
            model = revertChannelType(state.model, channelId)
        )
    }

    fun updateDeletedChannel(state: PlayWidgetState, channelId: String): PlayWidgetState {
        return state.copy(
            model = deleteChannelWidget(state.model, channelId)
        )
    }

    fun updateActionReminder(state: PlayWidgetState, channelId: String, reminderType: PlayWidgetReminderType): PlayWidgetState {
        return state.copy(
            model = updateWidgetActionReminder(state.model, channelId, reminderType)
        )
    }

    /**
     * Private methods
     */
    private fun updateWidgetTotalView(model: PlayWidgetUiModel, channelId: String, totalView: String): PlayWidgetUiModel {
        return model.copy(
            items = model.items.map { item ->
                if (item is PlayWidgetChannelUiModel && item.channelId == channelId) {
                    item.copy(
                        totalView = item.totalView.copy(totalViewFmt = totalView)
                    )
                } else item
            }
        )
    }

    private fun deleteChannelWidget(model: PlayWidgetUiModel, channelId: String): PlayWidgetUiModel {
        return model.copy(
            items = model.items.filter { item ->
                (item is PlayWidgetChannelUiModel && item.channelId != channelId) || item !is PlayWidgetChannelUiModel
            }
        )
    }

    private fun deletingChannelWidget(model: PlayWidgetUiModel, channelId: String): PlayWidgetUiModel {
        return model.copy(
            items = model.items.map { item ->
                if (item is PlayWidgetChannelUiModel && item.channelId == channelId) item.copy(channelType = PlayWidgetChannelType.Deleting)
                else item
            }
        )
    }

    private fun revertChannelType(model: PlayWidgetUiModel, channelId: String): PlayWidgetUiModel {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetChannelUiModel && mediumWidget.channelId == channelId) {
                        val prevType = mediumWidget.channelTypeTransition.prevType ?: PlayWidgetChannelType.Unknown

                        mediumWidget.copy(
                                channelType = prevType,
                                channelTypeTransition = mediumWidget.channelTypeTransition.changeTo(prevType)
                        )
                    }
                    else mediumWidget
                }
        )
    }

    private fun updateWidgetActionReminder(model: PlayWidgetUiModel, channelId: String, reminderType: PlayWidgetReminderType): PlayWidgetUiModel {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetChannelUiModel && mediumWidget.channelId == channelId) mediumWidget.copy(reminderType = reminderType)
                    else mediumWidget
                }
        )
    }
}
