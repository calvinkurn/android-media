package com.tokopedia.gifting.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gifting.domain.model.GetAddOnByID
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GiftingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getAddOnUseCase: GetAddOnUseCase
) : BaseViewModel(dispatchers.main) {

    private val mGetAddOnResult = MutableLiveData<GetAddOnByID>()
    val getAddOnResult: LiveData<GetAddOnByID> get() = mGetAddOnResult

    private val mErrorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable> get() = mErrorThrowable

    fun getAddOn(addOnId: Long) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnUseCase.setParams(addOnId.toString())
                getAddOnUseCase.executeOnBackground().getAddOnByID
            }
            mGetAddOnResult.value = result
            result.error.let {
                if (it.messages.isNotEmpty()) {
                    mErrorThrowable.value = MessageErrorException(it.messages, it.errorCode)
                }
            }
        }, onError = {
            mErrorThrowable.value = it
        })
    }
}