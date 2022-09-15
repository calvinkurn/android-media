package com.tokopedia.play.model

import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsBasicInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayProductTagsUiModel

/**
 * Created by jegul on 09/02/21
 */
class PlayProductTagsModelBuilder {

    fun buildBasicInfo(
            bottomSheetTitle: String = "abc",
            partnerId: Long = 123L
    ) = PlayProductTagsBasicInfoUiModel(
            maxFeaturedProducts = 5,
            bottomSheetTitle = bottomSheetTitle,
            partnerId = partnerId
    )

    fun buildProductLine(
        id: String = "1",
        shopId: String = "123",
        imageUrl: String = "https://www.tokopedia.com",
        title: String = "Barang Murah",
        stock: ProductStock = OutOfStock,
        isVariantAvailable: Boolean = false,
        price: ProductPrice = buildOriginalProductPrice(),
        minQty: Int = 1,
        isFreeShipping: Boolean = false,
        applink: String? = null,
        isTokoNow: Boolean = false,
        isPinned: Boolean = false,
        isRilisanSpesial: Boolean = false,
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
        applink = applink,
        isTokoNow = isTokoNow,
        isPinned = isPinned,
        isRilisanSpesial = isRilisanSpesial,
    )

    fun buildMerchantVoucher(
        type: MerchantVoucherType = MerchantVoucherType.Discount,
        title: String = "Diskon 10%",
        description: String = "Min. Pembelanjaan 10rb",
        expiredDate: String = "2018-12-07T23:30:00Z",
        voucherStock: Int = 0,
        isPrivate: Boolean = false,
    ) = PlayVoucherUiModel.Merchant(
        type = type,
        title = title,
        description = description,
        id = "1",
        code = "123",
        copyable = true,
        highlighted = true,
        expiredDate = expiredDate,
        voucherStock = voucherStock,
        isPrivate = isPrivate,
    )

    fun buildOriginalProductPrice(
            price: String = "123",
            priceNumber: Double = 123.0
    ) = OriginalPrice(
            price = price,
            priceNumber = priceNumber
    )

    fun buildIncompleteData(
            basicInfo: PlayProductTagsBasicInfoUiModel = buildBasicInfo()
    ) = PlayProductTagsUiModel.Incomplete(basicInfo = basicInfo)

    fun buildCompleteData(
            basicInfo: PlayProductTagsBasicInfoUiModel = buildBasicInfo(),
            productList: List<PlayProductUiModel> = List(5) { buildProductLine(id = it.toString()) },
            voucherList: List<PlayVoucherUiModel> = List(3) { buildMerchantVoucher() }
    ) = PlayProductTagsUiModel.Complete(
            basicInfo = basicInfo,
            productList = productList,
            voucherList = voucherList
    )
}
