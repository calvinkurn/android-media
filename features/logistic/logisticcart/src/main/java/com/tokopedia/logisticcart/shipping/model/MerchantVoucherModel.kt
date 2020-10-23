package com.tokopedia.logisticcart.shipping.model

data class MerchantVoucherModel(
        var isMvc: Int = 0,
        var mvcTitle: String = "",
        var mvcLogo: String = "",
        var mvcErrorMessage: String = ""
)