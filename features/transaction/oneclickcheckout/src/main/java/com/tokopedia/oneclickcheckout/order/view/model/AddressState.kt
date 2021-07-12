package com.tokopedia.oneclickcheckout.order.view.model

data class AddressState(
        var errorCode: String = "",
        var address: OrderProfileAddress = OrderProfileAddress(),
        var popupMessage: String = ""
) {
    companion object {
        const val ERROR_CODE_OPEN_ADDRESS_LIST = "4"
        const val ERROR_CODE_OPEN_ANA = "3"
    }
}