package com.tokopedia.buy_more_get_more.minicart.presentation.model

import com.tokopedia.bmsm_widget.presentation.model.GiftWidgetState
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.buy_more_get_more.common.OfferType
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

/**
 * Created by @ilhamsuaib on 14/08/23.
 */

data class BmgmMiniCartDataUiModel(
    val offerId: Long = BmgmCommonDataModel.NON_DISCOUNT_TIER_ID,
    val offerType: OfferType = OfferType.PROGRESSIVE_DISCOUNT,
    val offerMessage: List<String> = listOf(),
    val hasReachMaxDiscount: Boolean = false,
    val tiers: List<BmgmMiniCartVisitable> = emptyList(),
    val priceBeforeBenefit: Double = 0.0,
    val finalPrice: Double = 0.0,
    val isTierAchieved: Boolean = false,
    val products: List<Product> = emptyList(),
    val offerJsonData: String = "",
    val cartString: String = ""
)

internal fun BmgmMiniCartDataUiModel.getProductList(): List<BmgmMiniCartVisitable> {
    return when (offerType) {
        OfferType.PROGRESSIVE_DISCOUNT -> getProgressiveDiscountProductList(this)
        OfferType.GIFT_WITH_PURCHASE -> getGiftWithPurchaseItems(this)
    }
}

private fun getGiftWithPurchaseItems(detail: BmgmMiniCartDataUiModel): List<BmgmMiniCartVisitable> {
    val items = mutableListOf<BmgmMiniCartVisitable>()
    val tiers = detail.tiers.filterIsInstance<BmgmMiniCartVisitable.TierUiModel>()

    val tierProducts = tiers.flatMap { it.products }
    val giftList = tiers.flatMap { it.productsBenefit }
    if (detail.isTierAchieved) {
        if (giftList.isNotEmpty()) {
            val benefitWording = tiers.firstNotNullOf { it.benefitWording }
            val benefitCta = tiers.firstNotNullOf { it.benefitCta }
            val giftWidgetModel = BmgmMiniCartVisitable.GwpGiftWidgetUiModel(
                state = GiftWidgetState.SUCCESS,
                benefitWording = benefitWording,
                benefitCta = benefitCta,
                productList = giftList.map {
                    ProductGiftUiModel(
                        id = it.productId,
                        qty = it.quantity,
                        name = it.productName,
                        imageUrl = it.productImage,
                        isUnlocked = true
                    )
                }
            )
            items.add(giftWidgetModel)
        }
    } else {
        val giftPlaceholder = giftList.firstOrNull() ?: tierProducts.maxByOrNull { it.quantity }
        val productImage = giftPlaceholder?.productImage.orEmpty()
        items.add(BmgmMiniCartVisitable.GwpGiftPlaceholder(productImage))
    }
    tierProducts.forEach { product ->
        repeat(product.quantity) {
            items.add(product)
        }
    }
    return items.toList()
}

private fun getProgressiveDiscountProductList(data: BmgmMiniCartDataUiModel): List<BmgmMiniCartVisitable> {
    val productList = mutableListOf<BmgmMiniCartVisitable>()
    data.tiers.forEach { t ->
        if (t is BmgmMiniCartVisitable.TierUiModel) {
            if (t.isDiscountTier()) {
                productList.add(t)
            } else {
                t.products.forEach { product ->
                    repeat(product.quantity) {
                        productList.add(product)
                    }
                }
            }
        }
    }
    if (!data.hasReachMaxDiscount) {
        productList.add(BmgmMiniCartVisitable.PlaceholderUiModel)
    }
    return productList.toList()
}
