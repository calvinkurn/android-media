package com.tokopedia.smartbills.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.common_digital.common.constant.DigitalUrl
import com.tokopedia.common_digital.common.data.request.DataRequest
import com.tokopedia.smartbills.data.DataRechargeMultiCheckoutResponse
import com.tokopedia.smartbills.data.MultiCheckoutRequest
import java.lang.reflect.Type
import javax.inject.Inject

class SmartBillsMultiCheckoutUseCase @Inject constructor(val repository: RestRepository) : RestRequestUseCase(repository) {

    var request: MultiCheckoutRequest  = MultiCheckoutRequest()
    var headers = mutableMapOf<String, String>()

    fun setParam(request: MultiCheckoutRequest) {
        this.request = request
    }

    fun setHeader(headers: HashMap<String, String>){
        this.headers = headers
    }

    private val url = DigitalUrl.BASE_URL + PATH_MULTI_CHECKOUT

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, DataRechargeMultiCheckoutResponse::class.java)
                .setRequestType(RequestType.POST)
                .setBody(DataRequest(request))
                .setHeaders(headers)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    companion object {
        const val PATH_MULTI_CHECKOUT = "checkout/multi-item"
    }
}