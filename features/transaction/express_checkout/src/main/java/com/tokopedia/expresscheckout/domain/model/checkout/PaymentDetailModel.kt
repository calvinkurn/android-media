package com.tokopedia.expresscheckout.domain.model.checkout

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class PaymentDetailModel(
        var name: String? = null,
        var amount: Int = 0
)