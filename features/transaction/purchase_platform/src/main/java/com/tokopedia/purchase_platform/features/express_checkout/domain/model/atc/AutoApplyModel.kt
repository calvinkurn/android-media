package com.tokopedia.purchase_platform.features.express_checkout.domain.model.atc

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class AutoApplyModel(
        var isSuccess: Boolean? = false,
        var code: String? = null,
        var isCoupon: Int = 0,
        var discountAmount: Int = 0,
        var titleDescription: String? = null,
        var messageSuccess: String? = null,
        var promoId: Int = 0
)