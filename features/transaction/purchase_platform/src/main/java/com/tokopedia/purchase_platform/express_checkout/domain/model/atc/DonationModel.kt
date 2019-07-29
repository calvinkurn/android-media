package com.tokopedia.purchase_platform.express_checkout.domain.model.atc

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class DonationModel(
        var title: String? = null,
        var nominal: Int = 0,
        var description: String? = null
)