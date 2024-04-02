package com.tokopedia.logisticseller.ui.confirmshipping.ui

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingErrorState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingErrorStateSource
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingEvent
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingResult
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.ChangeCourierUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.GetConfirmShippingResultUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase.GetCourierListUseCase
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.targetedticker.domain.TargetedTickerMapper
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ConfirmShippingComposeViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val somGetConfirmShippingResultUseCase: GetConfirmShippingResultUseCase,
    private val somGetCourierListUseCase: GetCourierListUseCase,
    private val somChangeCourierUseCase: ChangeCourierUseCase,
    private val targetedTickerUseCase: GetTargetedTickerUseCase
) : BaseViewModel(dispatcher.io) {

    private val _uiState = MutableStateFlow(ConfirmShippingState())
    val uiState: StateFlow<ConfirmShippingState> = _uiState.asStateFlow()
    private val _error = MutableSharedFlow<ConfirmShippingErrorState>(replay = 1)
    val error: SharedFlow<ConfirmShippingErrorState> = _error.asSharedFlow()
    private val _result = MutableSharedFlow<ConfirmShippingResult>(replay = 1)
    val result: SharedFlow<ConfirmShippingResult> = _result.asSharedFlow()

    fun onEvent(event: ConfirmShippingEvent) {
        when (event) {
            is ConfirmShippingEvent.ChooseCourier -> {
                chooseCourier(event)
            }

            is ConfirmShippingEvent.ChooseService -> {
                chooseService(event)
            }

            ConfirmShippingEvent.Loading -> {
                _uiState.update {
                    it.copy(loading = true)
                }
            }

            is ConfirmShippingEvent.OnCreate -> {
                _uiState.update {
                    it.copy(mode = event.mode)
                }
                getCourierList(event.orderId)
            }

            is ConfirmShippingEvent.Submit -> {
                _uiState.update {
                    when (it.mode) {
                        ConfirmShippingMode.CHANGE_COURIER -> {
                            changeCourier(
                                orderId = event.orderId,
                                shippingRef = it.referenceNumber,
                                agencyId = it.chosenCourier?.shipmentId.toLongOrZero(),
                                spId = it.chosenService?.spId.toLongOrZero()
                            )
                        }

                        ConfirmShippingMode.CONFIRM_SHIPPING -> {
                            confirmShipping(event.orderId, it.referenceNumber)
                        }
                    }
                    it.copy(loading = true)
                }
            }

            is ConfirmShippingEvent.SwitchMode -> {
                _uiState.update {
                    it.copy(mode = if (event.isChangeCourier) ConfirmShippingMode.CHANGE_COURIER else ConfirmShippingMode.CONFIRM_SHIPPING)
                }
            }

            is ConfirmShippingEvent.ChangeRefNum -> {
                _uiState.update {
                    it.copy(referenceNumber = event.refNum)
                }
            }
        }
    }

    private fun chooseService(event: ConfirmShippingEvent.ChooseService) {
        _uiState.update {
            it.copy(chosenService = event.service)
        }
    }

    private fun chooseCourier(event: ConfirmShippingEvent.ChooseCourier) {
        _uiState.update {
            val defaultService = event.courier.listShipmentPackage.firstOrNull()
            it.copy(chosenCourier = event.courier, chosenService = defaultService)
        }
    }

    private fun confirmShipping(orderId: String, shippingRef: String) {
        launchCatchError(block = {
            val confirmShippingResult = somGetConfirmShippingResultUseCase.execute(
                orderId,
                shippingRef
            )
            _uiState.update {
                it.copy(loading = false)
            }
            _result.emit(ConfirmShippingResult.ShippingConfirmed(confirmShippingResult.listMessage.firstOrNull()))
        }, onError = {
            _uiState.update { state -> state.copy(loading = false) }
            _result.emit(ConfirmShippingResult.FailedConfirmShipping(it))
        })
    }

    private fun getCourierList(orderId: String) {
        launchCatchError(block = {
            val result = somGetCourierListUseCase.execute(orderId)
            val chosenCourier = result.listShipment.firstOrNull()
            val chosenService = chosenCourier?.listShipmentPackage?.firstOrNull()
            _uiState.update {
                it.copy(
                    loading = false,
                    courierList = result.listShipment,
                    chosenCourier = chosenCourier,
                    chosenService = chosenService
                )
            }
            getTickerData(result.tickerUnificationParams)
        }, onError = {
            _uiState.update { state -> state.copy(loading = false) }
            _error.emit(ConfirmShippingErrorState(it, ConfirmShippingErrorStateSource.COURIER_LIST))
        })
    }

    private fun getTickerData(tickerUnificationTargets: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.TickerUnificationParams) {
        launchCatchError(
            block = {
                val template = TargetedTickerParamModel.Template().copy(
                    contents = tickerUnificationTargets.template.contents.map {
                        TargetedTickerParamModel.Template.Content(it.key, it.value)
                    }
                )
                val target = tickerUnificationTargets.target.map {
                    TargetedTickerParamModel.Target(it.type, it.values)
                }
                val param = TargetedTickerParamModel(
                    page = tickerUnificationTargets.page,
                    target = target,
                    template = template
                )
                val response = targetedTickerUseCase(param)
                val model =
                    TargetedTickerMapper.convertTargetedTickerToUiModel(response.getTargetedTickerData)
                _uiState.update {
                    it.copy(tickerData = model)
                }
            },
            onError = {
                _error.emit(
                    ConfirmShippingErrorState(
                        it,
                        ConfirmShippingErrorStateSource.TARGETED_TICKER
                    )
                )
            })
    }

    private fun changeCourier(orderId: String, shippingRef: String, agencyId: Long, spId: Long) {
        launchCatchError(block = {
            val changeCourierResult = somChangeCourierUseCase.execute(
                orderId,
                shippingRef,
                agencyId,
                spId
            )
            _uiState.update {
                it.copy(loading = false)
            }
            _result.emit(ConfirmShippingResult.CourierChanged(changeCourierResult.mpLogisticChangeCourier.listMessage.firstOrNull()))
        }, onError = {
            _uiState.update { state -> state.copy(loading = false) }
            _result.emit(ConfirmShippingResult.FailedChangeCourier(it))
        })
    }
}
