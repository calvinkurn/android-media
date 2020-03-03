package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.ProductSheetContent

/**
 * Created by jegul on 03/03/20
 */
data class ProductSheetUiModel(
        val title: String,
        val contentList: List<ProductSheetContent>
)