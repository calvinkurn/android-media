package com.tokopedia.play.model.tagitem

import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play_common.model.result.ResultState

interface TagItemBuilder {

    fun buildTagItem(
        product: ProductUiModel = buildProduct(),
        voucher: VoucherUiModel = buildVoucher(),
        maxFeatured: Int = 0,
        resultState: ResultState = ResultState.Success,
    ): TagItemUiModel

    fun buildProduct(
        productList: List<PlayProductUiModel.Product> = emptyList(),
        canShow: Boolean = false,
    ): ProductUiModel

    fun buildVoucher(
        voucherList: List<MerchantVoucherUiModel> = emptyList()
    ): VoucherUiModel
}