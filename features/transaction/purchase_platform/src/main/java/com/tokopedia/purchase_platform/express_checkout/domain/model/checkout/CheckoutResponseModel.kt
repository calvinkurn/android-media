package com.tokopedia.purchase_platform.express_checkout.domain.model.checkout

import com.tokopedia.purchase_platform.express_checkout.domain.model.HeaderModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class CheckoutResponseModel(
        var headerModel: HeaderModel? = null,
        var checkoutDataModel: CheckoutDataModel? = null,
        var status: String? = null
)