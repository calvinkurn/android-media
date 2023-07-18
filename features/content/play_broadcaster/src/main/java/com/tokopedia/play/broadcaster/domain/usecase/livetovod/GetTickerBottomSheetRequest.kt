package com.tokopedia.play.broadcaster.domain.usecase.livetovod

import com.tokopedia.play.broadcaster.ui.model.livetovod.TickerBottomSheetPageType

data class GetTickerBottomSheetRequest(
    val page: TickerBottomSheetPageType,
)
