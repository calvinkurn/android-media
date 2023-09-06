package com.tokopedia.logisticseller.ui.requestpickup.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.domain.usecase.ConfirmRequestPickupUseCase
import com.tokopedia.logisticseller.ui.requestpickup.domain.usecase.ProcessRequestPickupUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class RequestPickupViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val confirmRequestPickupUseCase: ConfirmRequestPickupUseCase,
    private val processRequestPickupUseCase: ProcessRequestPickupUseCase
) : BaseViewModel(dispatcher.io) {

    private val _confirmReqPickupResult = MutableLiveData<Result<SomConfirmReqPickup.Data>>()
    val confirmReqPickupResult: LiveData<Result<SomConfirmReqPickup.Data>>
        get() = _confirmReqPickupResult

    private val _processReqPickupResult = MutableLiveData<Result<SomProcessReqPickup.Data>>()
    val processReqPickupResult: LiveData<Result<SomProcessReqPickup.Data>>
        get() = _processReqPickupResult

    fun loadConfirmRequestPickup(reqPickupParam: SomConfirmReqPickupParam) {
        launchCatchError(block = {
            _confirmReqPickupResult.postValue(Success(confirmRequestPickupUseCase.execute(reqPickupParam)))
        }, onError = {
            _confirmReqPickupResult.postValue(Fail(it))
        })
    }

    fun processRequestPickup(processReqPickupParam: SomProcessReqPickupParam) {
        launchCatchError(block = {
            _processReqPickupResult.postValue(Success(processRequestPickupUseCase.execute(processReqPickupParam)))
        }, onError = {
            _processReqPickupResult.postValue(Fail(it))
        })
    }
}
