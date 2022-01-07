package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class DigitalPDPPulsaViewModel @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _dummy = MutableLiveData<Result<Boolean>>()
    val dummy: LiveData<Result<Boolean>>
        get() = _dummy

    private var debounceJob: Job? = null

    private val _errorMessage = MutableLiveData<Result<String>>()
    val errorMessage: LiveData<Result<String>>
        get() = _errorMessage

    fun getDelayedResponse() {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(coroutineContext).launch {
            launchCatchError(block = {
                delay(1000)
                _dummy.postValue(Success(true))
            }) {

            }
        }
    }
}