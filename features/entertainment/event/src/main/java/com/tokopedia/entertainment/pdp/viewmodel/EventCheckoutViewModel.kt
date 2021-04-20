package com.tokopedia.entertainment.pdp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.entertainment.common.util.EventQuery
import com.tokopedia.entertainment.pdp.data.EventProductDetailEntity
import com.tokopedia.entertainment.pdp.data.checkout.CheckoutGeneralV2InstantParams
import com.tokopedia.entertainment.pdp.data.checkout.CheckoutGeneralV2Params
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutInstantResponse
import com.tokopedia.entertainment.pdp.data.checkout.EventCheckoutResponse
import com.tokopedia.entertainment.pdp.data.pdp.EventPDPErrorEntity
import com.tokopedia.entertainment.pdp.usecase.EventProductDetailUseCase
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EventCheckoutViewModel @Inject constructor(private val dispatcher: CoroutineDispatcher,
                                                 private val usecase: EventProductDetailUseCase,
                                                 private val graphqlRepository: GraphqlRepository
) : BaseViewModel(dispatcher) {

    private val eventProductDetailMutable = MutableLiveData<EventProductDetailEntity>()
    val eventProductDetail: LiveData<EventProductDetailEntity>
        get() = eventProductDetailMutable

    private val isErrorMutable = MutableLiveData<EventPDPErrorEntity>()
    val isError: LiveData<EventPDPErrorEntity>
        get() = isErrorMutable

    private val errorGeneralValueMutable = MutableLiveData<Throwable>()
    val errorGeneralValue: LiveData<Throwable>
        get() = errorGeneralValueMutable

    private val errorValueMutable = MutableLiveData<String>()
    val errorValue: LiveData<String>
        get() = errorValueMutable

    private val eventCheckoutResponseMutable = MutableLiveData<EventCheckoutResponse>()
    val eventCheckoutResponse: LiveData<EventCheckoutResponse>
        get() = eventCheckoutResponseMutable

    private val eventCheckoutInstantResponseMutable = MutableLiveData<EventCheckoutInstantResponse>()
    val eventCheckoutInstantResponse: LiveData<EventCheckoutInstantResponse>
        get() = eventCheckoutInstantResponseMutable

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

    fun checkoutEvent(rawQuery:String, checkoutGeneralV2Params: CheckoutGeneralV2Params) {
        launchCatchError(block = {

            val params = mapOf(PARAM to checkoutGeneralV2Params)
            val graphqlRequest = GraphqlRequest(rawQuery,EventCheckoutResponse::class.java, params)

            val response = withContext(dispatcher) { graphqlRepository.getReseponse(listOf(graphqlRequest)) }
            eventCheckoutResponseMutable.value = response.getSuccessData<EventCheckoutResponse>()
        }) {
            errorGeneralValueMutable.postValue(it)
        }
    }

    fun checkoutEventInstant(checkoutGeneralV2InstantParams: CheckoutGeneralV2InstantParams) {
        launchCatchError(block = {

            val params = mapOf(PARAM to checkoutGeneralV2InstantParams)
            val graphqlRequest = GraphqlRequest(EventQuery.mutationEventCheckoutInstant(),EventCheckoutInstantResponse::class.java, params)

            val response = withContext(dispatcher) { graphqlRepository.getReseponse(listOf(graphqlRequest)) }
            eventCheckoutInstantResponseMutable.value = response.getSuccessData<EventCheckoutInstantResponse>()
        }) {
            errorGeneralValueMutable.postValue(it)
        }
    }


    companion object {
        const val PARAM = "params"
    }
}