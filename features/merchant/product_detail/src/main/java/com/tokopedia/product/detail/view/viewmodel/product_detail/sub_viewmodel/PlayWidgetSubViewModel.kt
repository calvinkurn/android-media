package com.tokopedia.product.detail.view.viewmodel.product_detail.sub_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.widget.domain.PlayWidgetUseCase
import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.switch
import com.tokopedia.play.widget.util.PlayWidgetTools
import com.tokopedia.product.detail.view.viewmodel.product_detail.IPlayWidgetSubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.base.SubViewModel
import com.tokopedia.product.detail.view.viewmodel.product_detail.mediator.GetProductDetailDataMediator
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by yovi.putra on 30/03/23"
 * Project name: android-tokopedia-core
 **/

class PlayWidgetSubViewModel @Inject constructor(
    private val playWidgetTools: PlayWidgetTools
) : SubViewModel(), IPlayWidgetSubViewModel {

    val mediator by lazy { getMediator<GetProductDetailDataMediator>() }

    private val _playWidgetModel = MutableLiveData<Result<PlayWidgetState>>()
    override val playWidgetModel: LiveData<Result<PlayWidgetState>>
        get() = _playWidgetModel

    private val _playWidgetReminderSwitch = MutableLiveData<Result<PlayWidgetReminderType>>()
    override val playWidgetReminderSwitch: LiveData<Result<PlayWidgetReminderType>>
        get() = _playWidgetReminderSwitch

    override fun getPlayWidgetData() {
        launch {
            runCatching {
                val productIds = mediator.getVariant()?.let { variant ->
                    listOf(variant.parentId) + variant.children.map { it.productId }
                } ?: emptyList()
                val categoryIds = mediator.getPdpLayout()?.basic?.category?.detail?.map {
                    it.id
                } ?: emptyList()

                val widgetType = PlayWidgetUseCase.WidgetType.PDPWidget(
                    productIds,
                    categoryIds
                )
                val response = playWidgetTools.getWidgetFromNetwork(widgetType)
                val uiModel = playWidgetTools.mapWidgetToModel(response)
                _playWidgetModel.value = Success(uiModel)
            }.onFailure {
                _playWidgetModel.value = Fail(it)
            }
        }
    }

    override fun updatePlayWidgetToggleReminder(
        playWidgetState: PlayWidgetState,
        channelId: String,
        reminderType: PlayWidgetReminderType
    ) {
        launch {
            runCatching {
                val updatedUi = playWidgetTools.updateActionReminder(
                    playWidgetState,
                    channelId,
                    reminderType
                )
                _playWidgetModel.value = Success(updatedUi)

                val response = playWidgetTools.updateToggleReminder(channelId, reminderType)
                if (playWidgetTools.mapWidgetToggleReminder(response)) {
                    _playWidgetReminderSwitch.value = Success(reminderType)
                } else {
                    val reversedToggleUi = playWidgetTools.updateActionReminder(
                        playWidgetState,
                        channelId,
                        reminderType.switch()
                    )
                    _playWidgetModel.value = Success(reversedToggleUi)
                    _playWidgetReminderSwitch.value = Fail(Throwable())
                }
            }.onFailure {
                val reversedToggleUi = playWidgetTools.updateActionReminder(
                    playWidgetState,
                    channelId,
                    reminderType.switch()
                )
                _playWidgetModel.value = Success(reversedToggleUi)
                _playWidgetReminderSwitch.value = Fail(it)
            }
        }
    }
}
