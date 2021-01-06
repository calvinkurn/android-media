package com.tokopedia.pms.howtopay_native.domain

import com.tokopedia.pms.howtopay_native.data.model.HowToPayInstruction
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

private const val GATEWAY_CODE = "gatewayCode"
private const val GATEWAY_NAME = "gateway_name"
private const val BANK_CODE = "bank_code"
private const val PAYMENT_TYPE = "payment_type"
private const val TRANSACTION_CODE = "transaction_code"
private const val TRANSACTION_AMOUNT = "TRANSACTION_AMOUNT"

class GetHowToPayInstructions/* : UseCase<HowToPayInstruction>() {

    override suspend fun executeOnBackground(): HowToPayInstruction {
         useCaseRequestParams
    }


    fun getRequestParam(gatewayCode : String, gatewayName : String, bankCode : String){
        val requestParams = RequestParams.create()
        requestParams.putAllString(mapOf(GATEWAY_CODE to gatewayCode, GATEWAY_NAME to gatewayName))
    }
}*/