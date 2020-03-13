package com.tokopedia.play.view.uimodel

import com.tokopedia.play.view.type.PlayProductUiModel

/**
 * Created by jegul on 03/03/20
 */
data class ProductSheetUiModel(
        val title: String,
        val voucherList: List<PlayVoucherUiModel>,
        val productList: List<PlayProductUiModel>
)