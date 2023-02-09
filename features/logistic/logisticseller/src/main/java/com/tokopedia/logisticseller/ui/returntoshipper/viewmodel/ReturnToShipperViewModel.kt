package com.tokopedia.logisticseller.ui.returntoshipper.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticseller.data.param.GeneralInfoRtsParam
import com.tokopedia.logisticseller.data.response.GetGeneralInfoRtsResponse
import com.tokopedia.logisticseller.domain.usecase.GetGeneralInfoRtsUseCase
import com.tokopedia.logisticseller.domain.usecase.RequestGeneralInfoRtsUseCase
import com.tokopedia.logisticseller.ui.returntoshipper.uimodel.ReturnToShipperState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReturnToShipperViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getGeneralInfoRtsUseCase: GetGeneralInfoRtsUseCase,
    private val requestGeneralInfoRtsUseCase: RequestGeneralInfoRtsUseCase
) : BaseViewModel(dispatcher.main) {
    private val _confirmationRtsState =
        MutableLiveData<ReturnToShipperState<GetGeneralInfoRtsResponse.GeneralInfoRtsData>>()
    val confirmationRtsState: LiveData<ReturnToShipperState<GetGeneralInfoRtsResponse.GeneralInfoRtsData>>
        get() = _confirmationRtsState

    fun getGeneralInformation(orderId: String) {
        launchCatchError(
            block = {
                _confirmationRtsState.value = ReturnToShipperState.ShowLoading(true)
                val response = getGeneralInfoRtsUseCase(
                    GeneralInfoRtsParam(
                        input = GeneralInfoRtsParam.GeneralInfoRtsInput(
                            orderId = orderId,
                            action = GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION
                        )
                    )
                )
                _confirmationRtsState.value = ReturnToShipperState.ShowLoading(false)

                if (response.isSuccess && response.generalInformation.data != null) {
                    _confirmationRtsState.value =
                        ReturnToShipperState.ShowRtsConfirmDialog(response.generalInformation.data)
                } else {
                    _confirmationRtsState.value =
                        ReturnToShipperState.ShowToaster(response.messageError)
                }
            },
            onError = {
                _confirmationRtsState.value = ReturnToShipperState.ShowLoading(false)
                _confirmationRtsState.value = ReturnToShipperState.ShowToaster(it.message.orEmpty())
            }
        )
    }

    fun requestGeneralInformation(orderId: String, action: String) {
        val isActionConfirmation = isActionConfirmation(action)

        launchCatchError(
            block = {
                if (isActionConfirmation) {
                    _confirmationRtsState.value = ReturnToShipperState.ShowLoading(true)
                }
                val response = requestGeneralInfoRtsUseCase(
                    GeneralInfoRtsParam(
                        input = GeneralInfoRtsParam.GeneralInfoRtsInput(
                            orderId = orderId,
                            action = action
                        )
                    )
                )

                if (isActionConfirmation) {
                    _confirmationRtsState.value = ReturnToShipperState.ShowLoading(false)

                    if (response.isSuccess) {
                        _confirmationRtsState.value =
                            ReturnToShipperState.ShowRtsSuccessDialog
                    } else {
                        _confirmationRtsState.value =
                            ReturnToShipperState.ShowRtsFailedDialog
                    }
                }
            },
            onError = {
                if (isActionConfirmation) {
                    _confirmationRtsState.value = ReturnToShipperState.ShowLoading(false)
                    _confirmationRtsState.value =
                        ReturnToShipperState.ShowToaster(it.message.orEmpty())
                }
            }
        )
    }

    private fun isActionConfirmation(action: String): Boolean =
        action == GeneralInfoRtsParam.ACTION_RTS_CONFIRMATION

}
