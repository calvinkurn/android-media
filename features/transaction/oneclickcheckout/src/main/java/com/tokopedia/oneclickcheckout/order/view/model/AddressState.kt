package com.tokopedia.oneclickcheckout.order.view.model

data class AddressState(
        var errorCode: String = "",
        var state: Int = 0,
        var popupMessage: String = ""
) {
    companion object {
        const val IS_ERROR = "4"
        const val STATE_DEFAULT = 0
        const val STATE_ADDRESS_ID_MATCH_DEFAULT_OCC = 201
        const val STATE_ADDRESS_ID_MATCH_NON_DEFAULT_OCC = 202
        const val STATE_ADDRESS_ID_NOT_MATCH_ANY_OCC = 203
        const val STATE_DISTRICT_ID_MATCH_DEFAULT_OCC = 204
        const val STATE_DISTRICT_ID_MATCH_NON_DEFAULT_OCC = 205
        const val STATE_DISTRICT_ID_NOT_MATCH_ANY_OCC = 206
    }
}