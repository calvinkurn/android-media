package com.tokopedia.minicart.common.domain.data

import com.tokopedia.minicart.common.data.response.undodeletecart.UndoDeleteCartDataResponse

data class UndoDeleteCartDomainModel(
        var undoDeleteCartDataResponse: UndoDeleteCartDataResponse,
        var isLastItem: Boolean = false
)