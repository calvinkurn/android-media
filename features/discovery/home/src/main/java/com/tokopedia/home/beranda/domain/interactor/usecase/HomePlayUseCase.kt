package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.util.PlayWidgetTools
import dagger.Lazy
import javax.inject.Inject

class HomePlayUseCase @Inject constructor(
        private val homeDispatcher: Lazy<CoroutineDispatchers>,
        private val playWidgetTools: PlayWidgetTools) {
    fun onUpdateActionReminder(
            currentCarouselPlayWidgetDataModel: CarouselPlayWidgetDataModel,
            channelId: String,
            isReminder: Boolean
    ): CarouselPlayWidgetDataModel {
        val reminderType = if(isReminder) PlayWidgetReminderType.Reminded else PlayWidgetReminderType.NotReminded
        return currentCarouselPlayWidgetDataModel.copy(widgetState = playWidgetTools.updateActionReminder(currentCarouselPlayWidgetDataModel.widgetState, channelId, reminderType))
    }

    fun onGetPlayWidgetUiModel(
            playWidgetState: PlayWidgetState,
            channelId: String,
            reminderType: PlayWidgetReminderType
    ): PlayWidgetState {
        return playWidgetTools.updateActionReminder(playWidgetState, channelId, reminderType)
    }

    fun onUpdatePlayTotalView(
            currentCarouselPlayWidgetDataModel: CarouselPlayWidgetDataModel,
            channelId: String,
            totalView: String
    ): CarouselPlayWidgetDataModel {
        return currentCarouselPlayWidgetDataModel.copy(widgetState = playWidgetTools.updateTotalView(currentCarouselPlayWidgetDataModel.widgetState, channelId, totalView))
    }

   suspend fun onGetPlayWidgetWhenShouldRefresh(): PlayWidgetState {
       val response = playWidgetTools.getWidgetFromNetwork(
                PlayWidgetUseCase.WidgetType.Home,
                homeDispatcher.get().io)
       return playWidgetTools.mapWidgetToModel(response)
    }

    suspend fun onUpdatePlayWidgetToggleReminder(
            channelId: String,
            reminderType: PlayWidgetReminderType): Boolean {
        return try {
            val response = playWidgetTools.updateToggleReminder(
                    channelId,
                    reminderType,
                    homeDispatcher.get().io
            )
            playWidgetTools.mapWidgetToggleReminder(response)
        } catch (e: Exception) {
            false
        }
    }
}