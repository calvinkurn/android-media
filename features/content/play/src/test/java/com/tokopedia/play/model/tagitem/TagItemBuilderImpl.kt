package com.tokopedia.play.model.tagitem

import com.tokopedia.play.view.type.MerchantVoucherType
import com.tokopedia.play.view.type.ProductPrice
import com.tokopedia.play.view.type.ProductStock
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play_common.model.result.ResultState

class TagItemBuilderImpl : TagItemBuilder {

    override fun buildTagItem(
        product: ProductUiModel,
        voucher: VoucherUiModel,
        maxFeatured: Int,
        bottomSheetTitle: String,
        resultState: ResultState
    ) = TagItemUiModel(
        product = product,
        voucher = voucher,
        maxFeatured = maxFeatured,
        bottomSheetTitle = bottomSheetTitle,
        resultState = resultState,
    )

    override fun buildProductModel(
        productList: List<ProductSectionUiModel>,
        canShow: Boolean
    ) = ProductUiModel(
        productSectionList = productList,
        canShow = canShow,
    )

    override fun buildVoucherModel(
        voucherList: List<MerchantVoucherUiModel>
    ) = VoucherUiModel(
        voucherList = voucherList,
    )

    override fun buildProduct(
        id: String,
        shopId: String,
        imageUrl: String,
        title: String,
        stock: ProductStock,
        isVariantAvailable: Boolean,
        price: ProductPrice,
        minQty: Int,
        isFreeShipping: Boolean,
        appLink: String
    ) = PlayProductUiModel.Product(
        id = id,
        shopId = shopId,
        imageUrl = imageUrl,
        title = title,
        stock = stock,
        isVariantAvailable = isVariantAvailable,
        price = price,
        minQty = minQty,
        isFreeShipping = isFreeShipping,
        applink = appLink,
    )

    override fun buildMerchantVoucher(
        id: String,
        type: MerchantVoucherType,
        title: String,
        description: String,
        code: String,
        copyable: Boolean,
        highlighted: Boolean,
        voucherStock: Int,
        expiredDate: String
    ) = MerchantVoucherUiModel(
        id = id,
        type = type,
        title = title,
        description = description,
        code = code,
        copyable = copyable,
        highlighted = highlighted,
        voucherStock = voucherStock,
        expiredDate = expiredDate,
    )

    override fun buildProductSection(
        productList: List<PlayProductUiModel.Product>,
        config: ProductSectionUiModel.ConfigUiModel
    ) =  ProductSectionUiModel(
        productList = productList,
        config = config
    )
}