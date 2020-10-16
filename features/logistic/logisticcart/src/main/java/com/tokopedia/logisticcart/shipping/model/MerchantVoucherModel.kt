package com.tokopedia.logisticcart.shipping.model

data class MerchantVoucherModel(
        var isMvc: Boolean = false,
        var mvcTitle: String = "",
        var mvcLogo: String = "",
        var mvcErrorMessage: String = ""
)