package com.tokopedia.play.model.tagitem

import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play_common.model.result.ResultState

class TagItemBuilderImpl : TagItemBuilder {

    override fun buildTagItem(
        product: ProductUiModel,
        voucher: VoucherUiModel,
        maxFeatured: Int,
        resultState: ResultState
    ) = TagItemUiModel(
        product = product,
        voucher = voucher,
        maxFeatured = maxFeatured,
        resultState = resultState,
    )

    override fun buildProduct(
        productList: List<PlayProductUiModel.Product>,
        canShow: Boolean
    ) = ProductUiModel(
        productList = productList,
        canShow = canShow,
    )

    override fun buildVoucher(
        voucherList: List<MerchantVoucherUiModel>
    ) = VoucherUiModel(
        voucherList = voucherList,
    )
}