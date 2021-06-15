package com.tokopedia.minicart.common.domain.data

import com.tokopedia.minicart.common.data.response.deletecart.RemoveFromCartData

data class RemoveFromCartUiModel(
        var removeFromCartData: RemoveFromCartData = RemoveFromCartData(),
        var isLastItem: Boolean = false
)