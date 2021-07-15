package com.tokopedia.cart_common.domain.data

import com.tokopedia.cart_common.data.response.undodeletecart.UndoDeleteCartDataResponse

data class UndoDeleteCartDomainModel(
        var undoDeleteCartDataResponse: UndoDeleteCartDataResponse,
        var isLastItem: Boolean = false
)