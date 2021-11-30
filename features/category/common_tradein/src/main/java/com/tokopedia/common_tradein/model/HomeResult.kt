package com.tokopedia.common_tradein.model

class HomeResult {
    var displayMessage: String = ""
    var deviceDisplayName: String = ""
    var isSuccess = false
    var priceStatus: PriceState? = null
    var minPrice: Int? = null
    var maxPrice: Int? = null

    enum class PriceState {
        DIAGNOSED_VALID, DIAGNOSED_INVALID, NOT_DIAGNOSED, MONEYIN_ERROR
    }
}