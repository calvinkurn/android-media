package com.tokopedia.buyerorder.recharge.data.request

/**
 * @author by furqan on 26/10/2021
 */
class RechargeOrderDetailRequest(
        val orderCategory: String,
        val orderId: String,
        val upstream: String? = null
)