package com.tokopedia.chatbot.chatbot2.csat.view

import android.util.Log
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chatbot.chatbot2.csat.domain.model.CsatModel
import com.tokopedia.chatbot.chatbot2.csat.domain.model.PointModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class CsatViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _csatDataStateFlow = MutableStateFlow(CsatModel())
    val csatDataStateFlow = _csatDataStateFlow.asStateFlow()


    fun initializeData(csatModel: CsatModel) {
        _csatDataStateFlow.update {
            it.copy(
                title = csatModel.title,
                points = csatModel.points,
                selectedPoint = csatModel.selectedPoint
            )
        }
    }

    fun setSelectedScore(pointModel: PointModel) {
        _csatDataStateFlow.update {
            it.copy(
                selectedPoint = pointModel
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
        updateButton()
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
        updateButton()
    }

    fun setOtherReason(otherReason: String) {
        val minimumOtherReasonChar = 30
        if (otherReason.length < minimumOtherReasonChar) {
            _csatDataStateFlow.value.isOtherReasonError = true
        } else {
            _csatDataStateFlow.value.otherReason = otherReason
        }
        Log.d("Irfan", _csatDataStateFlow.value.otherReason)
        updateButton()
    }

    private fun updateButton() {
        val minimumOtherReasonChar = 30
        _csatDataStateFlow.value.let {
            if (it.selectedReasons.isEmpty() || it.otherReason.length < minimumOtherReasonChar) {
                _csatDataStateFlow.update {
                    it.copy(isButtonEnabled = false)
                }
            } else {
                _csatDataStateFlow.update {
                    it.copy(isButtonEnabled = true)
                }
            }
        }
    }

}
