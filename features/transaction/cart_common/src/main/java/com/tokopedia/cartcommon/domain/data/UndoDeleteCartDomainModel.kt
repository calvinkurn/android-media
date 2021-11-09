package com.tokopedia.cartcommon.domain.data

import com.tokopedia.cartcommon.data.response.undodeletecart.UndoDeleteCartDataResponse

data class UndoDeleteCartDomainModel(
        var undoDeleteCartDataResponse: UndoDeleteCartDataResponse,
        var isLastItem: Boolean = false
)