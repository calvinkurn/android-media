package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class MessagesModel(
        var errorCheckoutPriceLimit: String? = null,
        var errorFieldBetween: String? = null,
        var errorFieldMaxChar: String? = null,
        var errorFieldRequired: String? = null,
        var errorProductAvailableStock: String? = null,
        var errorProductAvailableStockDetail: String? = null,
        var errorProductMaxQuantity: String? = null,
        var errorProductMinQuantity: String? = null
)