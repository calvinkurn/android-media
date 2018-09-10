package com.tokopedia.payment.setting.add.domain

import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.payment.setting.add.model.ResponseGetIFrameCreditCard
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.payment.setting.util.PAYMENT_SETTING_URL

class AddCreditCardUseCase : RestRequestUseCase() {

    override fun buildRequest(): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()

        val restRequest1 = RestRequest.Builder(PAYMENT_SETTING_URL, ResponseGetIFrameCreditCard::class.java)
                .setBody(HashMap<String, Any>())
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restRequest1)

        return tempRequest
    }
}