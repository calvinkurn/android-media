package com.tokopedia.play.view.uimodel

/**
 * Created by jegul on 03/03/20
 */
data class ProductSheetUiModel(
        val title: String,
        val partnerId: Long,
        val voucherList: List<PlayVoucherUiModel>,
        val productList: List<PlayProductUiModel>
) {

    companion object {
        fun empty() = ProductSheetUiModel(
                title = "",
                partnerId = 0L,
                voucherList = emptyList(),
                productList = emptyList()
        )
    }
}