package com.tokopedia.buyerorderdetail.presentation.adapter.listener

interface CourierButtonListener {
    fun initGroupBooking(
        gojekOrderId: String,
        source: String
    )

    fun onChatButtonClicked(
        gojekOrderId: String,
        source: String,
        counter: String
    )
}
