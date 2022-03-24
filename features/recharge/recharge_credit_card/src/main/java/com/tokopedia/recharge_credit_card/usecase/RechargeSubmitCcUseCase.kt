package com.tokopedia.recharge_credit_card.usecase

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.authentication.HEADER_X_TKPD_APP_VERSION
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
                .setHeaders(getPcidssCustomHeaders())
                .setBody(mapParam)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    private fun getPcidssCustomHeaders(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers[HEADER_X_TKPD_APP_VERSION] = "android-" + GlobalConfig.VERSION_NAME
        return headers
    }

    companion object {
        const val creditCardUrl =  "digital/creditcard/iframe"
    }
}
