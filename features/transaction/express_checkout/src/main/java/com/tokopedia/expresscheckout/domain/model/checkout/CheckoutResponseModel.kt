package com.tokopedia.expresscheckout.domain.model.checkout

import com.tokopedia.expresscheckout.domain.model.HeaderModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class CheckoutResponseModel(
        var headerModel: HeaderModel? = null,
        var checkoutDataModel: CheckoutDataModel? = null,
        var status: String? = null
)