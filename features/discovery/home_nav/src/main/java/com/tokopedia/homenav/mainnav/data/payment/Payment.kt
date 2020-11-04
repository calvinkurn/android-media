package com.tokopedia.homenav.mainnav.data.payment

data class Payment(
    val gateway_img: String,
    val gateway_name: String,
    val merchant_code: String,
    val payment_amount: Int,
    val product_name: String,
    val ticker_message: String,
    val transaction_id: String
)