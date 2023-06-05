package com.tokopedia.logisticseller.ui.reschedulepickup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.domain.mapper.ReschedulePickupMapper
import com.tokopedia.logisticseller.domain.usecase.GetReschedulePickupUseCase
import com.tokopedia.logisticseller.domain.usecase.SaveReschedulePickupUseCase
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.RescheduleErrorAction
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupAction
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupErrorState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInput
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReschedulePickupViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getReschedulePickup: GetReschedulePickupUseCase,
    private val saveReschedulePickup: SaveReschedulePickupUseCase
) : BaseViewModel(dispatcher.main) {

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "Data Reschedule Pickup tidak ditemukan"
        private const val BELOW_MIN_CHAR_ERROR_MESSAGE = "Min. 15 karakter"
        private const val ABOVE_MAX_CHAR_ERROR_MESSAGE = "Sudah mencapai maks. char"
        private const val OTHER_REASON_RESCHEDULE = "Lainnya (Isi Sendiri)"
        private const val OTHER_REASON_MIN_CHAR = 15
        private const val OTHER_REASON_MAX_CHAR = 160
    }

    private var orderId: String = ""
    private val _uiState = MutableStateFlow(ReschedulePickupState())
    val uiState: StateFlow<ReschedulePickupState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ReschedulePickupAction>(replay = 1)
    val uiEffect: SharedFlow<ReschedulePickupAction> = _uiEffect.asSharedFlow()

    var input by mutableStateOf(ReschedulePickupInput())
        private set

    fun onEvent(event: ReschedulePickupUiEvent) {
        when (event) {
            is ReschedulePickupUiEvent.SaveReschedule -> saveReschedule(orderId)
            is ReschedulePickupUiEvent.LoadRescheduleInfo -> {
                orderId = event.orderId
                getReschedulePickupDetail(event.orderId)
            }
            is ReschedulePickupUiEvent.SelectDay -> setDay(event.selectedDay)
            is ReschedulePickupUiEvent.SelectTime -> setTime(event.selectedTime)
            is ReschedulePickupUiEvent.SelectReason -> setReason(event.selectedReason)
            is ReschedulePickupUiEvent.CustomReason -> setCustomReason(event.reason)
            is ReschedulePickupUiEvent.ClickSubtitle -> setAction(
                ReschedulePickupAction.OpenTnCWebView(
                    event.url
                )
            )
            is ReschedulePickupUiEvent.CloseDialog -> {
                setDialogState(false)
                if (event.success) {
                    setAction(ReschedulePickupAction.ClosePage(true))
                }
            }
            is ReschedulePickupUiEvent.PressBack -> setAction(ReschedulePickupAction.ClosePage(false))
        }
    }

    private fun setAction(action: ReschedulePickupAction) = viewModelScope.launch {
        _uiEffect.emit(action)
    }

    private fun getReschedulePickupDetail(orderId: String) {
        launchCatchError(
            block = {
                val response = getReschedulePickup(
                    ReschedulePickupMapper.mapToGetReschedulePickupParam(
                        listOf(orderId)
                    )
                )
                if (response.mpLogisticGetReschedulePickup.data.isNotEmpty()) {
                    val errorMessage =
                        response.mpLogisticGetReschedulePickup.data.first().orderData.firstOrNull()?.errorMessage
                    if (errorMessage?.isNotEmpty() == true) {
                        _uiEffect.emit(
                            ReschedulePickupAction.ShowError(
                                ReschedulePickupErrorState(
                                    message = errorMessage,
                                    action = RescheduleErrorAction.SHOW_TOASTER_FAILED_GET_RESCHEDULE
                                )
                            )
                        )
                    } else {
                        _uiState.update {
                            ReschedulePickupMapper.mapToState(response.mpLogisticGetReschedulePickup)
                        }
                    }
                } else {
                    _uiEffect.emit(
                        ReschedulePickupAction.ShowError(
                            ReschedulePickupErrorState(
                                message = DEFAULT_ERROR_MESSAGE,
                                action = RescheduleErrorAction.SHOW_EMPTY_STATE
                            )
                        )
                    )
                }
            },
            onError = {
                _uiEffect.emit(
                    ReschedulePickupAction.ShowError(
                        ReschedulePickupErrorState(
                            message = it.message.orEmpty(),
                            action = RescheduleErrorAction.SHOW_EMPTY_STATE
                        )
                    )
                )
            }
        )
    }

    private fun setDay(day: RescheduleDayOptionModel) {
        input = input.copy(day = day.day, time = "")
        val state = _uiState.value
        _uiState.update {
            it.copy(
                options = state.options.copy(timeOptions = day.timeOptions),
                info = state.info.copy(summary = "")
            )
        }
        validateInput()
    }

    private fun setTime(time: RescheduleTimeOptionModel) {
        input = input.copy(time = time.time)
        _uiState.update { it.copy(info = it.info.copy(summary = time.etaPickup)) }
        validateInput()
    }

    private fun setReason(reason: RescheduleReasonOptionModel) {
        _uiState.update {
            it.copy(
                reason = reason.reason,
                isCustomReason = reason.reason == OTHER_REASON_RESCHEDULE
            )
        }
        if (reason.reason != OTHER_REASON_RESCHEDULE) {
            input = input.copy(reason = reason.reason)
        } else {
            input = input.copy(reason = "")
        }
        validateInput()
    }

    private fun setCustomReason(reason: String) {
        input = input.copy(reason = reason)
        validateCustomReason(reason)
        validateInput()
    }

    private fun setDialogState(open: Boolean) {
        _uiState.update {
            it.copy(saveRescheduleModel = it.saveRescheduleModel?.copy(openDialog = open))
        }
    }

    private fun validateInput() {
        val state = _uiState.value
        val isValid =
            input.day.isNotEmpty() && input.time.isNotEmpty() && validateReason(
                input.reason,
                state.reason
            )
        _uiState.update { it.copy(valid = isValid) }
    }

    private fun validateReason(reason: String, templateReason: String): Boolean {
        return if (templateReason == OTHER_REASON_RESCHEDULE) {
            reason.length in OTHER_REASON_MIN_CHAR..OTHER_REASON_MAX_CHAR
        } else {
            reason.isNotEmpty()
        }
    }

    private fun validateCustomReason(reason: String) {
        val error =
            if (reason.length < OTHER_REASON_MIN_CHAR) {
                BELOW_MIN_CHAR_ERROR_MESSAGE
            } else if (reason.length >= OTHER_REASON_MAX_CHAR) {
                ABOVE_MAX_CHAR_ERROR_MESSAGE
            } else {
                null
            }
        _uiState.update { it.copy(customReasonError = error) }
    }

    private fun saveReschedule(
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
                _uiState.update {
                    it.copy(
                        saveRescheduleModel = ReschedulePickupMapper.mapToSaveRescheduleModel(
                            response,
                            it.info.summary,
                            orderId
                        )
                    )
                }
            },
            onError = {
                _uiEffect.emit(
                    ReschedulePickupAction.ShowError(
                        ReschedulePickupErrorState(
                            message = it.message.orEmpty(),
                            action = RescheduleErrorAction.SHOW_TOASTER_FAILED_SAVE_RESCHEDULE
                        )
                    )
                )
            }
        )
    }
}
