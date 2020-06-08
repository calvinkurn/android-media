package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutBody
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.checkout.mapper.EventCheckoutMapper.mapToCart
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPErrorEntity
import com.tokopedia.entertainment.pdp.network_api.EventCheckoutRepository
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.promocheckout.common.domain.model.event.Cart
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyBody
import com.tokopedia.promocheckout.common.domain.model.event.EventVerifyResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventCheckoutViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                                 private val usecase: EventProductDetailUseCase,
                                                 private val repository: EventCheckoutRepository
) : BaseViewModel(dispatcher) {

    private val eventProductDetailMutable = MutableLiveData<EventProductDetailEntity>()
    val eventProductDetail: LiveData<EventProductDetailEntity>
        get() = eventProductDetailMutable

    private val isErrorMutable = MutableLiveData<EventPDPErrorEntity>()
    val isError: LiveData<EventPDPErrorEntity>
        get() = isErrorMutable

    private val eventVerifyResponseMutable = MutableLiveData<EventVerifyResponse>()
    val eventVerifyResponse: LiveData<EventVerifyResponse>
        get() = eventVerifyResponseMutable

    private val errorGeneralValueMutable = MutableLiveData<Throwable>()
    val errorGeneralValue: LiveData<Throwable>
        get() = errorGeneralValueMutable

    private val errorValueMutable = MutableLiveData<String>()
    val errorValue: LiveData<String>
        get() = errorValueMutable

    private val eventCheckoutResponseMutable = MutableLiveData<EventCheckoutResponse>()
    val eventCheckoutResponse: LiveData<EventCheckoutResponse>
        get() = eventCheckoutResponseMutable

    fun getDataProductDetail(rawQueryPDP: String, rawQueryContent: String, urlPdp: String) {
        launch {
            val result = usecase.executeUseCase(rawQueryPDP, rawQueryContent, true, urlPdp)
            when (result) {
                is Success -> {
                    eventProductDetailMutable.value = result.data.eventProductDetailEntity
                }
                is Fail -> {
                    isErrorMutable.value = EventPDPErrorEntity(true, result.throwable.message)
                }
            }
        }
    }

    fun checkVerify(book: Boolean, eventVerifyBody: EventVerifyBody) {
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                repository.postVerify(createMapParam(book), eventVerifyBody)
            }

            if (data?.data?.status?.result.equals(SUCCESS, true)) {
                eventVerifyResponseMutable.postValue(data)
                checkCheckout(data?.data?.cart)
            } else {
                errorValueMutable.postValue(data?.data?.cart?.error)
            }

        }) {
            errorGeneralValueMutable.postValue(it)
        }
    }

    fun checkCheckout(cart: Cart?) {
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                cart?.let {
                    repository.postCheckout(mapToCart(cart))
                }
            }

            if (data?.data?.status.equals(SUCCESS, true)) {
                eventCheckoutResponseMutable.postValue(data)
            } else {
                errorValueMutable.postValue(data?.data?.error)
            }

        }) {
            errorGeneralValueMutable.postValue(it)
        }
    }

    fun createMapParam(book: Boolean): HashMap<String, Boolean> {
        val mapParam = HashMap<String, Boolean>()
        mapParam[BOOK] = book
        return mapParam
    }


    companion object {
        const val SUCCESS = "success"
        const val FAILURE = "failure"

        const val BOOK = "book"

        private const val ERROR_DEFAULT = "Terjadi kesalahan, silakan ulangi beberapa saat lagi"

    }
}