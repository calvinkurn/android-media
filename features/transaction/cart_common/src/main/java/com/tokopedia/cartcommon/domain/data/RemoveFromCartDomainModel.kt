package com.tokopedia.cartcommon.domain.data

import com.tokopedia.cartcommon.data.response.deletecart.RemoveFromCartData

data class RemoveFromCartDomainModel(
        var removeFromCartData: RemoveFromCartData = RemoveFromCartData(),
        var isLastItem: Boolean = false,
        var isBulkDelete: Boolean = false
)