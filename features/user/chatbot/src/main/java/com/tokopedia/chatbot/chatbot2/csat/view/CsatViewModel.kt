package com.tokopedia.chatbot.chatbot2.csat.view

import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.chatbot2.csat.data.request.SubmitCsatRequest
import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import com.tokopedia.chatbot.chatbot2.csat.domain.usecase.SubmitCsatUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CsatViewModel @Inject constructor(
    private val submitCsatUseCase: SubmitCsatUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _csatEventFlow = MutableSharedFlow<CsatEvent>(replay = 1)
    val csatEventFlow = _csatEventFlow.asSharedFlow()

    private val _csatDataStateFlow = MutableStateFlow(CsatModel())
    val csatDataStateFlow = _csatDataStateFlow.asStateFlow()

    fun processAction(action: CsatAction) {
        when (action) {
            is CsatAction.SelectScore -> setSelectedScore(action.pointModel)
            is CsatAction.SelectReason -> selectSelectedReason(action.reason)
            is CsatAction.UnselectReason -> unselectSelectedReason(action.reason)
            is CsatAction.SetOtherReason -> setOtherReason(action.reason)
            CsatAction.SendCsat -> sendCsat()
        }
    }

    fun initializeData(selectedScore: Int, csatModel: CsatModel) {
        val selectedPoint = csatModel.points.firstOrNull { it.score == selectedScore }
        if (selectedPoint != null) {
            _csatDataStateFlow.update {
                it.copy(
                    title = csatModel.title,
                    points = csatModel.points,
                    selectedPoint = selectedPoint,
                    selectedReasons = mutableListOf(),
                    otherReason = "",
                    minimumOtherReasonChar = csatModel.minimumOtherReasonChar,
                    maximumOtherReasonChar = csatModel.maximumOtherReasonChar
                )
            }
        } else {
            _csatEventFlow.tryEmit(CsatEvent.FallbackDismissBottomSheet)
        }
    }

    private fun setSelectedScore(pointModel: PointModel) {
        _csatDataStateFlow.update {
            it.copy(
                selectedPoint = pointModel,
                selectedReasons = mutableListOf()
            )
        }
    }

    private fun selectSelectedReason(reason: String) {
        _csatDataStateFlow.value.selectedReasons.let {
            if (!it.contains(reason)) {
                it.add(reason)
            }
        }

        _csatDataStateFlow.value.selectedReasons.forEach {
            Log.d("Irfan", it)
        }
    }

    private fun unselectSelectedReason(reason: String) {
        _csatDataStateFlow.value.selectedReasons.let {
            if (it.contains(reason)) {
                it.remove(reason)
            }
        }

        _csatDataStateFlow.value.selectedReasons.forEach {
            Log.d("Irfan", it)
        }
    }

    private fun setOtherReason(otherReason: String) {
        _csatDataStateFlow.value.otherReason = otherReason
        Log.d("Irfan", _csatDataStateFlow.value.otherReason)
    }

    fun updateButton() {
        val minimumOtherReasonChar = 30
        _csatDataStateFlow.value.let { csatModel ->
            val isSelectedReasonsEmpty = csatModel.selectedReasons.isEmpty()
            val isOtherReasonNotSatisfied =
                csatModel.otherReason.isNotBlank() && csatModel.otherReason.length < minimumOtherReasonChar
            if (isSelectedReasonsEmpty || isOtherReasonNotSatisfied) {
                _csatEventFlow.tryEmit(CsatEvent.UpdateButton(isEnabled = false))
            } else {
                _csatEventFlow.tryEmit(CsatEvent.UpdateButton(isEnabled = true))
            }
        }
    }

    private fun sendCsat() {
        submitCsatUseCase.setRequestParams(SubmitCsatRequest())
        submitCsatUseCase.execute(
            {
                Log.d("Irfan", "Send Csat Success")
            },
            {
                Log.d("Irfan", "Send Csat Error")
            }
        )
    }
}
