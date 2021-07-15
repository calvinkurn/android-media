package com.tokopedia.cart_common.domain.data

import com.tokopedia.cart_common.data.response.deletecart.RemoveFromCartData

data class RemoveFromCartDomainModel(
        var removeFromCartData: RemoveFromCartData = RemoveFromCartData(),
        var isLastItem: Boolean = false,
        var isBulkDelete: Boolean = false
)