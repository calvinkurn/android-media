package com.tokopedia.chatbot.chatbot2.csat.view

import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.SubmitButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CsatViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _csatDataStateFlow = MutableStateFlow(CsatModel())
    val csatDataStateFlow = _csatDataStateFlow.asStateFlow()

    private val _submitButtonStateFlow = MutableStateFlow(SubmitButtonState())
    val submitButtonStateFlow = _submitButtonStateFlow.asStateFlow()

    fun initializeData(csatModel: CsatModel) {
        _csatDataStateFlow.update {
            it.copy(
                title = csatModel.title,
                points = csatModel.points,
                selectedPoint = csatModel.selectedPoint,
                selectedReasons = mutableListOf()
            )
        }
    }

    fun setSelectedScore(pointModel: PointModel) {
        _csatDataStateFlow.update {
            it.copy(
                selectedPoint = pointModel,
                selectedReasons = mutableListOf()
            )
        }
    }

    fun selectSelectedReason(reason: String) {
        _csatDataStateFlow.value.selectedReasons.let {
            if (!it.contains(reason)) {
                it.add(reason)
            }
        }

        _csatDataStateFlow.value.selectedReasons.forEach {
            Log.d("Irfan", it)
        }
    }

    fun unselectSelectedReason(reason: String) {
        _csatDataStateFlow.value.selectedReasons.let {
            if (it.contains(reason)) {
                it.remove(reason)
            }
        }

        _csatDataStateFlow.value.selectedReasons.forEach {
            Log.d("Irfan", it)
        }
    }

    fun setOtherReason(otherReason: String) {
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
                _submitButtonStateFlow.update {
                    it.copy(
                        isEnabled = false
                    )
                }
            } else {
                _submitButtonStateFlow.update {
                    it.copy(
                        isEnabled = true
                    )
                }
            }
        }
    }

}
