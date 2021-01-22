package com.tokopedia.play.view.uimodel.recom

import com.tokopedia.play.view.uimodel.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.PinnedProductUiModel

/**
 * Created by jegul on 21/01/21
 */
data class PlayPinnedInfoUiModel(
        val pinnedMessage: PinnedMessageUiModel?,
        val pinnedProduct: PinnedProductUiModel?
)