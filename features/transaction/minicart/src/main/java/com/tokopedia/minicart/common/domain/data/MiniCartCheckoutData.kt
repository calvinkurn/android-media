package com.tokopedia.minicart.common.domain.data

import com.tokopedia.cartcommon.data.response.common.OutOfService
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction

data class MiniCartCheckoutData(
    var errorMessage: String = "",
    var outOfService: OutOfService = OutOfService(),
    var toasterAction: ToasterAction = ToasterAction()
)
