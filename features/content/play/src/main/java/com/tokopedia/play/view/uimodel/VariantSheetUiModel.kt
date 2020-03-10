package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductLineUiModel

/**
 * Created by jegul on 06/03/20
 */
data class VariantSheetUiModel(
        val title: String,
        val product: ProductLineUiModel,
        val action: ProductAction
)