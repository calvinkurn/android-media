package com.tokopedia.play.widget.util

import com.tokopedia.play.widget.data.PlayWidget
import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.domain.PlayWidgetReminderUseCase
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMapper
import com.tokopedia.play.widget.ui.mapper.PlayWidgetMediumUiMapper
import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetSize
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
        private val mapperProviders: Map<PlayWidgetSize, @JvmSuppressWildcards PlayWidgetMapper>
){

    private val reminderUseCase: PlayWidgetReminderUseCase
        get() = lazyReminderUseCase.get()

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

    suspend fun setToggleReminder(channelId: String,
                                  remind: Boolean,
                                  coroutineContext: CoroutineContext = Dispatchers.IO): PlayWidgetReminder {
        return withContext(coroutineContext) {
            reminderUseCase.params = PlayWidgetReminderUseCase.createParams(channelId, remind)
            reminderUseCase.executeOnBackground()
        }
    }

    suspend fun mapWidgetToggleReminder(response: PlayWidgetReminder, coroutineContext: CoroutineContext = Dispatchers.Default): PlayWidgetReminderUiModel {
        return withContext(coroutineContext) {
            val mapper = mapperProviders[PlayWidgetSize.Medium]
            if (mapper is PlayWidgetMediumUiMapper) mapper.mapWidgetToggleReminder(response)
            else throw IllegalStateException("Mapper is not medium type")
        }
    }

    fun updateTotalView(model: PlayWidgetUiModel, channelId: String, totalView: String): PlayWidgetUiModel {
        return when (model) {
            is PlayWidgetUiModel.Small -> updateSmallWidgetTotalView(model, channelId, totalView)
            is PlayWidgetUiModel.Medium -> updateMediumWidgetTotalView(model, channelId, totalView)
            else -> model
        }
    }

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
}