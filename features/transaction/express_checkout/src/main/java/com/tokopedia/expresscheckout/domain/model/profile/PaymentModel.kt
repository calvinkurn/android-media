package com.tokopedia.expresscheckout.domain.model.profile

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class PaymentModel(
        var gatewayCode: String? = null,
        var checkoutParam: String? = null,
        var image: String? = null,
        var description: String? = null,
        var url: String? = null
)