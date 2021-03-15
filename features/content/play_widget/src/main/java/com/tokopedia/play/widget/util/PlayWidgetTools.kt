package com.tokopedia.play.widget.util

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUpdateChannelUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMediumUiMapper
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
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
        private val mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>
){

    private val reminderUseCase: PlayWidgetReminderUseCase
        get() = lazyReminderUseCase.get()

    private val updateChannelUseCase: PlayWidgetUpdateChannelUseCase
        get() = lazyUpdateChannelUseCase.get()

    suspend fun getWidgetFromNetwork(
            widgetType: PlayWidgetUseCase.WidgetType,
            coroutineContext: CoroutineContext = Dispatchers.IO): PlayWidget {
        return withContext(coroutineContext) {
            useCase.params = PlayWidgetUseCase.createParams(widgetType)
            useCase.executeOnBackground()
        }
    }

    suspend fun mapWidgetToModel(widgetResponse: PlayWidget, prevModel: PlayWidgetUiModel? = null, coroutineContext: CoroutineContext = Dispatchers.Default): PlayWidgetUiModel {
        return withContext(coroutineContext) {
            val mapper = mapperProviders[PlayWidgetSize.getByTypeString(widgetResponse.meta.template)] ?: throw IllegalStateException("Mapper cannot be null")
            mapper.mapWidget(widgetResponse, prevModel)
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
            val mapper = mapperProviders[PlayWidgetSize.Medium]
            if (mapper is PlayWidgetMediumUiMapper) mapper.mapWidgetToggleReminder(response)
            else throw IllegalStateException("Mapper is not medium type")
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

    fun updateTotalView(model: PlayWidgetUiModel, channelId: String, totalView: String): PlayWidgetUiModel {
        return when (model) {
            is PlayWidgetUiModel.Small -> updateSmallWidgetTotalView(model, channelId, totalView)
            is PlayWidgetUiModel.Medium -> updateMediumWidgetTotalView(model, channelId, totalView)
            else -> model
        }
    }

    fun updateDeletingChannel(model: PlayWidgetUiModel, channelId: String): PlayWidgetUiModel {
        return when (model) {
            is PlayWidgetUiModel.Small -> deletingChannelSmallWidget(model, channelId)
            is PlayWidgetUiModel.Medium -> deletingChannelMediumWidget(model, channelId)
            else -> model
        }
    }

    fun updateFailedDeletingChannel(model: PlayWidgetUiModel, channelId: String): PlayWidgetUiModel {
        return when (model) {
            is PlayWidgetUiModel.Medium -> revertChannelTypeMediumWidget(model, channelId)
            else -> model
        }
    }

    fun updateDeletedChannel(model: PlayWidgetUiModel, channelId: String): PlayWidgetUiModel {
        return when (model) {
            is PlayWidgetUiModel.Small -> deleteChannelSmallWidget(model, channelId)
            is PlayWidgetUiModel.Medium -> deleteChannelMediumWidget(model, channelId)
            else -> model
        }
    }

    fun updateActionReminder(model: PlayWidgetUiModel, channelId: String, reminderType: PlayWidgetReminderType): PlayWidgetUiModel {
        return when (model) {
            is PlayWidgetUiModel.Medium -> updateMediumWidgetActionReminder(model, channelId, reminderType)
            else -> model
        }
    }

    /**
     * Private methods
     */
    private fun updateSmallWidgetTotalView(model: PlayWidgetUiModel.Small, channelId: String, totalView: String): PlayWidgetUiModel.Small {
        return model.copy(
                items = model.items.map { smallWidget ->
                    if (smallWidget is PlayWidgetSmallChannelUiModel && smallWidget.channelId == channelId) smallWidget.copy(totalView = totalView)
                    else smallWidget
                }
        )
    }

    private fun updateMediumWidgetTotalView(model: PlayWidgetUiModel.Medium, channelId: String, totalView: String): PlayWidgetUiModel.Medium {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetMediumChannelUiModel && mediumWidget.channelId == channelId) mediumWidget.copy(totalView = totalView)
                    else mediumWidget
                }
        )
    }

    private fun deleteChannelSmallWidget(model: PlayWidgetUiModel.Small, channelId: String): PlayWidgetUiModel.Small {
        return model.copy(
                items = model.items.filter { smallWidget ->
                    (smallWidget is PlayWidgetSmallChannelUiModel && smallWidget.channelId != channelId) || smallWidget !is PlayWidgetSmallChannelUiModel
                }
        )
    }

    private fun deleteChannelMediumWidget(model: PlayWidgetUiModel.Medium, channelId: String): PlayWidgetUiModel.Medium {
        return model.copy(
                items = model.items.filter { mediumWidget ->
                    (mediumWidget is PlayWidgetMediumChannelUiModel && mediumWidget.channelId != channelId) || mediumWidget !is PlayWidgetMediumChannelUiModel
                }
        )
    }

    private fun deletingChannelSmallWidget(model: PlayWidgetUiModel.Small, channelId: String): PlayWidgetUiModel.Small {
        return model.copy(
                items = model.items.map { smallWidget ->
                    if (smallWidget is PlayWidgetSmallChannelUiModel && smallWidget.channelId == channelId) smallWidget.copy(channelType = PlayWidgetChannelType.Deleting)
                    else smallWidget
                }
        )
    }

    private fun deletingChannelMediumWidget(model: PlayWidgetUiModel.Medium, channelId: String): PlayWidgetUiModel.Medium {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetMediumChannelUiModel && mediumWidget.channelId == channelId) mediumWidget.copy(
                            channelType = PlayWidgetChannelType.Deleting,
                            channelTypeTransition = mediumWidget.channelTypeTransition.changeTo(PlayWidgetChannelType.Deleting)
                    )
                    else mediumWidget
                }
        )
    }

    private fun revertChannelTypeMediumWidget(model: PlayWidgetUiModel.Medium, channelId: String): PlayWidgetUiModel.Medium {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetMediumChannelUiModel && mediumWidget.channelId == channelId) {
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

    private fun updateMediumWidgetActionReminder(model: PlayWidgetUiModel.Medium, channelId: String, reminderType: PlayWidgetReminderType): PlayWidgetUiModel.Medium {
        return model.copy(
                items = model.items.map { mediumWidget ->
                    if (mediumWidget is PlayWidgetMediumChannelUiModel && mediumWidget.channelId == channelId) mediumWidget.copy(reminderType = reminderType)
                    else mediumWidget
                }
        )
    }
}