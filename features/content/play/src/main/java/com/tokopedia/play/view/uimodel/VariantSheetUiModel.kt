package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.ProductAction

/**
 * Created by jegul on 06/03/20
 */
data class VariantSheetUiModel(
        val title: String,
        val productId: String,
        val action: ProductAction
)