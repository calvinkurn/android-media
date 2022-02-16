package com.tokopedia.play.model.tagitem

import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.OutOfStock
import com.tokopedia.play.view.type.ProductPrice
import com.tokopedia.play.view.type.ProductStock
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play_common.model.result.ResultState

interface TagItemBuilder {

    fun buildTagItem(
        product: ProductUiModel = buildProductModel(),
        voucher: VoucherUiModel = buildVoucherModel(),
        maxFeatured: Int = 0,
        bottomSheetTitle: String = "",
        resultState: ResultState = ResultState.Success,
    ): TagItemUiModel

    fun buildProductModel(
        productList: List<ProductSectionUiModel> = emptyList(),
        canShow: Boolean = false,
    ): ProductUiModel

    fun buildVoucherModel(
        voucherList: List<MerchantVoucherUiModel> = emptyList()
    ): VoucherUiModel

    fun buildProduct(
        id: String = "",
        shopId: String = "",
        imageUrl: String = "",
        title: String = "",
        stock: ProductStock = OutOfStock,
        isVariantAvailable: Boolean = false,
        price: ProductPrice = OriginalPrice("0", 0.0),
        minQty: Int = 1,
        isFreeShipping: Boolean = false,
        appLink: String = ""
    ): PlayProductUiModel.Product

    fun buildMerchantVoucher(
        id: String = "",
        type: MerchantVoucherType = MerchantVoucherType.Private,
        title: String = "",
        description: String = "",
        code: String = "",
        copyable: Boolean = false,
        highlighted: Boolean = false,
        voucherStock: Int = 1,
        expiredDate: String = "",
    ): MerchantVoucherUiModel
}