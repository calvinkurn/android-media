package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.switch
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
        return currentCarouselPlayWidgetDataModel.copy(widgetUiModel = playWidgetTools.updateActionReminder(currentCarouselPlayWidgetDataModel.widgetUiModel, channelId, reminderType))
    }

    fun onGetPlayWidgetUiModel(
            playWidgetUiModel: PlayWidgetUiModel,
            channelId: String,
            reminderType: PlayWidgetReminderType
    ): PlayWidgetUiModel {
        return playWidgetTools.updateActionReminder(playWidgetUiModel, channelId, reminderType.switch())
    }

    fun onUpdatePlayTotalView(
            currentCarouselPlayWidgetDataModel: CarouselPlayWidgetDataModel,
            channelId: String,
            totalView: String
    ): CarouselPlayWidgetDataModel {
        return currentCarouselPlayWidgetDataModel.copy(widgetUiModel = playWidgetTools.updateTotalView(currentCarouselPlayWidgetDataModel.widgetUiModel, channelId, totalView))
    }

   suspend fun onGetPlayWidgetWhenShouldRefresh(): PlayWidgetUiModel {
       val response = playWidgetTools.getWidgetFromNetwork(
                PlayWidgetUseCase.WidgetType.Home,
                homeDispatcher.get().io)
       val uiModel = playWidgetTools.mapWidgetToModel(response)
       return uiModel
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