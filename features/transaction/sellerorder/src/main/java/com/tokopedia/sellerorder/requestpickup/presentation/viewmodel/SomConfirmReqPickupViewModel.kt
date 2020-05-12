package com.tokopedia.sellerorder.requestpickup.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.sellerorder.requestpickup.domain.SomConfirmReqPickupUseCase
import com.tokopedia.sellerorder.requestpickup.domain.SomProcessReqPickupUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                                       private val somConfirmReqPickupUseCase: SomConfirmReqPickupUseCase,
                                                       private val somProcessReqPickupUseCase: SomProcessReqPickupUseCase) : BaseViewModel(dispatcher.ui()) {
    private val _confirmReqPickupResult = MutableLiveData<Result<SomConfirmReqPickup.Data>>()
    val confirmReqPickupResult: LiveData<Result<SomConfirmReqPickup.Data>>
        get() = _confirmReqPickupResult

    private val _processReqPickupResult = MutableLiveData<Result<SomProcessReqPickup.Data>>()
    val processReqPickupResult: LiveData<Result<SomProcessReqPickup.Data>>
        get() = _processReqPickupResult

    fun loadConfirmRequestPickup(query: String, reqPickupParam: SomConfirmReqPickupParam) {
        launch {
            _confirmReqPickupResult.postValue(somConfirmReqPickupUseCase.execute(query, reqPickupParam))
        }
    }

    fun processRequestPickup(reqPickupQuery: String, processReqPickupParam: SomProcessReqPickupParam) {
        launch {
            _processReqPickupResult.postValue(somProcessReqPickupUseCase.execute(reqPickupQuery, processReqPickupParam))
        }
    }
}