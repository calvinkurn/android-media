package com.tokopedia.smartbills.data.api

import com.tokopedia.smartbills.data.MultiCheckoutRequest
import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse

interface SmartBillsRepository {
    suspend fun postMultiCheckout(request: MultiCheckoutRequest): RechargeMultiCheckoutResponse
}