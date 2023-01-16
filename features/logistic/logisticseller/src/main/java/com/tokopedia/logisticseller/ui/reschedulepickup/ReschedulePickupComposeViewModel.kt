package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.domain.mapper.ReschedulePickupMapper
import com.tokopedia.logisticseller.domain.usecase.GetReschedulePickupUseCase
import com.tokopedia.logisticseller.domain.usecase.SaveReschedulePickupUseCase
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleBottomSheetState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInput
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ReschedulePickupComposeViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getReschedulePickup: GetReschedulePickupUseCase,
    private val saveReschedulePickup: SaveReschedulePickupUseCase
) : BaseViewModel(dispatcher.main) {

    private val _uiState = MutableStateFlow(ReschedulePickupState())
    val uiState: StateFlow<ReschedulePickupState> = _uiState.asStateFlow()

    var input by mutableStateOf(ReschedulePickupInput())
        private set

    fun getReschedulePickupDetail(orderId: String) {
        launchCatchError(
            block = {
                val response = getReschedulePickup(
                    ReschedulePickupMapper.mapToGetReschedulePickupParam(
                        listOf(orderId)
                    )
                )
                if (response.mpLogisticGetReschedulePickup.data.isNotEmpty()) {
                    _uiState.value =
                        ReschedulePickupMapper.mapToState(response.mpLogisticGetReschedulePickup)
                } else {
                    _uiState.value =
                        ReschedulePickupState(error = "Data Reschedule Pickup tidak ditemukan")
                }
            },
            onError = { _uiState.value = ReschedulePickupState(error = it.message.orEmpty()) }
        )
    }

    fun setDay(day: RescheduleDayOptionModel) {
        input = input.copy(day = day.day, time = "")
        val state = _uiState.value
        _uiState.value = state.copy(
            options = state.options.copy(timeOptions = day.timeOptions),
            info = state.info.copy(summary = "")
        )
        validateInput()
    }

    fun setTime(time: RescheduleTimeOptionModel) {
        input = input.copy(time = time.time)
        val state = _uiState.value
        _uiState.value = state.copy(info = state.info.copy(summary = time.etaPickup))
        validateInput()
    }

    fun setReason(reason: RescheduleReasonOptionModel) {
        val state = _uiState.value
        _uiState.value = state.copy(
            reason = reason.reason,
            isCustomReason = reason.reason == OTHER_REASON_RESCHEDULE
        )
        if (reason.reason != OTHER_REASON_RESCHEDULE) {
            input = input.copy(reason = reason.reason)
        } else {
            input = input.copy(reason = "")
        }
        validateInput()
    }

    fun setCustomReason(reason: String) {
        input = input.copy(reason = reason)
        validateCustomReason(reason)
        validateInput()
    }

    private fun validateInput() {
        val isValid =
            input.day.isNotEmpty() && input.time.isNotEmpty() && validateReason(input.reason)
        val state = _uiState.value
        _uiState.value = state.copy(valid = isValid)
    }

    private fun validateReason(reason: String): Boolean {
        return if (reason == OTHER_REASON_RESCHEDULE) {
            reason.length in OTHER_REASON_MIN_CHAR..OTHER_REASON_MAX_CHAR
        } else {
            reason.isNotEmpty()
        }
    }

    private fun validateCustomReason(reason: String) {
        val state = _uiState.value
        val error =
            if (reason.length < OTHER_REASON_MIN_CHAR) {
                "Min. 15 karakter"
            } else if (reason.length >= OTHER_REASON_MAX_CHAR) {
                "Sudah mencapai maks. char"
            } else {
                null
            }
        _uiState.value = state.copy(customReasonError = error)
    }

    fun saveReschedule(
        orderId: String
    ) {
        launchCatchError(
            block = {
                val response = saveReschedulePickup(
                    ReschedulePickupMapper.mapToSaveReschedulePickupParam(
                        orderId,
                        input.day,
                        input.time,
                        input.reason
                    )
                )
                val state = _uiState.value
                _uiState.value = state.copy(
                    saveRescheduleModel = ReschedulePickupMapper.mapToSaveRescheduleModel(
                        response,
                        state.info.summary,
                        orderId
                    )
                )
            },
            onError = {
                val state = _uiState.value
                _uiState.value = state.copy(
                    error = it.message.orEmpty()
                )
            }
        )
    }

    fun closeBottomSheetState() {
        val state = _uiState.value
        if (state.bottomSheet != RescheduleBottomSheetState.NONE) {
            _uiState.value = state.copy(bottomSheet = RescheduleBottomSheetState.NONE)
        }
    }

    fun openBottomSheetState(it: RescheduleBottomSheetState) {
        val state = _uiState.value
        if (input.day.isEmpty() && it == RescheduleBottomSheetState.TIME) {
            closeBottomSheetState()
        } else {
            _uiState.value = state.copy(bottomSheet = it)
        }
    }

    companion object {
        private const val OTHER_REASON_RESCHEDULE = "Lainnya (Isi Sendiri)"
        private const val OTHER_REASON_MIN_CHAR = 15
        private const val OTHER_REASON_MAX_CHAR = 160
    }
}
