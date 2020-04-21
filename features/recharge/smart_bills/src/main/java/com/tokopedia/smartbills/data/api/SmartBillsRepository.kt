package com.tokopedia.smartbills.data.api

import com.tokopedia.smartbills.data.RechargeMultiCheckoutResponse

interface SmartBillsRepository {
    suspend fun postMultiCheckout(mapParam: Map<String, Any>,
                                  idempotencyKey: String,
                                  userId: String): RechargeMultiCheckoutResponse
}