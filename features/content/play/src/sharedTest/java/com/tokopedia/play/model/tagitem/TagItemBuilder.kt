package com.tokopedia.play.model.tagitem

import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.PlayVoucherUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.VoucherUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.utils.date.DateUtil
import java.util.*

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
        voucherList: List<PlayVoucherUiModel.Merchant> = emptyList()
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
        appLink: String = "",
        isTokoNow: Boolean = false,
        isPinned: Boolean = false,
        isRilisanSpesial: Boolean = false,
        buttons: List<ProductButtonUiModel> = emptyList(),
    ): PlayProductUiModel.Product

    fun buildMerchantVoucher(
        id: String = "",
        type: MerchantVoucherType = MerchantVoucherType.Discount,
        title: String = "",
        description: String = "",
        code: String = "",
        copyable: Boolean = false,
        highlighted: Boolean = false,
        voucherStock: Int = 1,
        expiredDate: String = "",
        isPrivate: Boolean = false,
    ): PlayVoucherUiModel.Merchant

    fun buildProductSection(
        productList: List<PlayProductUiModel.Product> = emptyList(),
        config: ProductSectionUiModel.Section.ConfigUiModel = buildSectionConfig(),
        id: String = "",
    ): ProductSectionUiModel.Section

    fun buildSectionConfig(
        type: ProductSectionType = ProductSectionType.Unknown,
        title: String = "",
        timerInfo: String = "",
        controlTime: Date = DateUtil.getCurrentDate(),
        serverTime: Date? = null,
        startTime: Date? = null,
        endTime: Date? = null,
        background: ProductSectionUiModel.Section.BackgroundUiModel =
            ProductSectionUiModel.Section.BackgroundUiModel(gradients = emptyList(), imageUrl = ""),
        reminderStatus: PlayUpcomingBellStatus = PlayUpcomingBellStatus.Unknown,
    ): ProductSectionUiModel.Section.ConfigUiModel

    fun buildButton(
        text: String = "",
        color: ProductButtonColor = ProductButtonColor.PRIMARY_BUTTON,
        type: ProductButtonType = ProductButtonType.ATC,
    ) : ProductButtonUiModel

    companion object {
        private const val DEFAULT_CAMPAIGN_ID = 3L
    }
}
