package com.tokopedia.play.view.uimodel.recom.tagitem

import com.tokopedia.play_common.model.result.ResultState

//TODO() = section instead of product
data class TagItemUiModel(
    val section: SectionUiModel,
    val product: ProductUiModel,
    val voucher: VoucherUiModel,
    val maxFeatured: Int,
    val resultState: ResultState,
) {
    companion object {
        val Empty: TagItemUiModel
            get() = TagItemUiModel(
                product = ProductUiModel.Empty,
                voucher = VoucherUiModel.Empty,
                maxFeatured = 0,
                resultState = ResultState.Loading,
                section = SectionUiModel.Empty
            )
    }
}