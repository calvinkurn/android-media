package com.tokopedia.play.model.tagitem

import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play_common.model.result.ResultState
import java.util.*

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
        voucherList: List<PlayVoucherUiModel.Merchant>
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
        appLink: String,
        isTokoNow: Boolean,
        isPinned: Boolean,
        isRilisanSpesial: Boolean,
        buttons: List<ProductButtonUiModel>,
        number: String,
        isNumerationShown: Boolean,
        rating: String,
        soldQuantity: String,
        label: PlayProductUiModel.Product.Label,
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
        isTokoNow = isTokoNow,
        isPinned = isPinned,
        isRilisanSpesial = isRilisanSpesial,
        buttons = buttons,
        number = number,
        isNumerationShown = isNumerationShown,
        rating = rating,
        soldQuantity = soldQuantity,
        label = label,
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
        expiredDate: String,
        isPrivate: Boolean,
    ) = PlayVoucherUiModel.Merchant(
        id = id,
        type = type,
        title = title,
        description = description,
        code = code,
        copyable = copyable,
        highlighted = highlighted,
        voucherStock = voucherStock,
        expiredDate = expiredDate,
        isPrivate = isPrivate,
    )

    override fun buildProductSection(
        productList: List<PlayProductUiModel.Product>,
        config: ProductSectionUiModel.Section.ConfigUiModel,
        id: String
    ) =  ProductSectionUiModel.Section(
        productList = productList,
        config = config,
        id = id,
    )

    override fun buildSectionConfig(
        type: ProductSectionType,
        title: String,
        timerInfo: String,
        controlTime: Date,
        serverTime: Date?,
        startTime: Date?,
        endTime: Date?,
        background: ProductSectionUiModel.Section.BackgroundUiModel,
        reminderStatus: PlayUpcomingBellStatus
    ) = ProductSectionUiModel.Section.ConfigUiModel (
        type = type,
        title = title,
        timerInfo = timerInfo,
        controlTime = controlTime,
        serverTime = serverTime,
        startTime = startTime,
        endTime = endTime,
        background = background,
        reminder = reminderStatus,
    )

    override fun buildButton(
        text: String,
        color: ProductButtonColor,
        type: ProductButtonType
    ): ProductButtonUiModel =
        ProductButtonUiModel(text, color, type)
}
