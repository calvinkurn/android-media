package com.tokopedia.recharge_credit_card.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.recharge_credit_card.datamodel.CCRedirectUrlResponse
import com.tokopedia.url.TokopediaUrl
import java.lang.reflect.Type
import javax.inject.Inject

class RechargeSubmitCcUseCase @Inject constructor(val repository: RestRepository) : RestRequestUseCase(repository) {

    var mapParam = mutableMapOf<String, String>()
    fun setMapParam(map: HashMap<String, String>) {
        this.mapParam = map
    }

    private val url = TokopediaUrl.getInstance().PCIDSS_CREDIT_CARD + creditCardUrl

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, CCRedirectUrlResponse::class.java)
                .setRequestType(RequestType.POST)
                .setBody(mapParam)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    companion object {
        const val creditCardUrl =  "digital/creditcard/iframe"
    }
}
