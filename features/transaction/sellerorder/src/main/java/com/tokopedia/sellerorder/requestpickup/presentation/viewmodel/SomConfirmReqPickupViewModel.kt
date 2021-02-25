package com.tokopedia.sellerorder.requestpickup.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.sellerorder.requestpickup.domain.SomConfirmReqPickupUseCase
import com.tokopedia.sellerorder.requestpickup.domain.SomProcessReqPickupUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                       private val somConfirmReqPickupUseCase: SomConfirmReqPickupUseCase,
                                                       private val somProcessReqPickupUseCase: SomProcessReqPickupUseCase) : BaseViewModel(dispatcher.io) {
    private val _confirmReqPickupResult = MutableLiveData<Result<SomConfirmReqPickup.Data>>()
    val confirmReqPickupResult: LiveData<Result<SomConfirmReqPickup.Data>>
        get() = _confirmReqPickupResult

    private val _processReqPickupResult = MutableLiveData<Result<SomProcessReqPickup.Data>>()
    val processReqPickupResult: LiveData<Result<SomProcessReqPickup.Data>>
        get() = _processReqPickupResult

    fun loadConfirmRequestPickup(query: String, reqPickupParam: SomConfirmReqPickupParam) {
        launchCatchError(block = {
            _confirmReqPickupResult.postValue(somConfirmReqPickupUseCase.execute(query, reqPickupParam))
        }, onError = {
            _confirmReqPickupResult.postValue(Fail(it))
        })
    }

    fun processRequestPickup(reqPickupQuery: String, processReqPickupParam: SomProcessReqPickupParam) {
        launchCatchError(block = {
            _processReqPickupResult.postValue(somProcessReqPickupUseCase.execute(reqPickupQuery, processReqPickupParam))
        }, onError = {
            _processReqPickupResult.postValue(Fail(it))
        })
    }
}