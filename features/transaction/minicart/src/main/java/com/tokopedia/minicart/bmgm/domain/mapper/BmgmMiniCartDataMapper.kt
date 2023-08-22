package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmData
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

class BmgmMiniCartDataMapper @Inject constructor() {

    companion object {
        private const val CART_DETAIL_TYPE = "group"
        private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2
    }

    fun mapToUiModel(data: MiniCartData): BmgmMiniCartDataUiModel {
        var bmgm: BmGmData? = null
        val productMap = mutableMapOf<String, Product>()
        data.data.availableSection.availableGroup.forEach { group ->
            val detail = group.cartDetails.firstOrNull { detail ->
                detail.cartDetailInfo.cartDetailType.equals(CART_DETAIL_TYPE, true)
            }
            if (detail != null) {
                bmgm = detail.cartDetailInfo.bmgmData
                detail.products.associateByTo(productMap) { it.productId }
                return@forEach
            }
        }
        bmgm?.let {
            val hasReachMaxDiscount = it.offerStatus == OFFER_STATUS_HAS_REACH_MAX_DISC
            return BmgmMiniCartDataUiModel(
                offerId = it.offerId,
                offerName = it.offerName,
                offerMessage = it.offerMessage,
                hasReachMaxDiscount = hasReachMaxDiscount,
                totalDiscount = it.totalDiscount,
                tiersApplied = it.tiersApplied.map { tier ->
                    BmgmMiniCartVisitable.TierUiModel(
                        tierId = tier.tierId,
                        tierMessage = tier.tierMessage,
                        tierDiscountStr = tier.tierDiscountText,
                        priceBeforeBenefit = tier.priceBeforeBenefit,
                        priceAfterBenefit = tier.priceAfterBenefit,
                        products = tier.listProduct.mapNotNull { tierProduct ->
                            productMap[tierProduct.productId]?.let { p ->
                                return@mapNotNull BmgmMiniCartVisitable.ProductUiModel(
                                    productId = p.productId,
                                    warehouseId = tierProduct.warehouseId.toString(),
                                    productName = p.productName,
                                    productImage = p.productImage.imageSrc100Square,
                                    finalPrice = tierProduct.finalPrice,
                                    quantity = tierProduct.qty
                                )
                            }
                            return@mapNotNull null
                        }
                    )
                }
            )
        }
        return BmgmMiniCartDataUiModel()
    }

    fun getDummy(): BmgmMiniCartDataUiModel {
        return BmgmMiniCartDataUiModel(
            offerId = 1,
            offerName = "Summer Sale",
            offerMessage = "Yay, kamu dapat <b>potongan Rp120 rb!</b>",
            totalDiscount = 50000.0,
            finalPrice = 3300000.0,
            priceBeforeBenefit = 3800000.0,
            hasReachMaxDiscount = false,
            showMiniCartFooter = true,
            tiersApplied = listOf(
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = 1,
                    tierDiscountStr = "diskon 25%",
                    priceBeforeBenefit = 1100000.0,
                    priceAfterBenefit = 1035000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            finalPrice = 50000001.0,
                            quantity = 1
                        ),
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            finalPrice = 50000001.0,
                            quantity = 1
                        ),
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            finalPrice = 50000001.0,
                            quantity = 1
                        )
                    )
                ),
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = BmgmCommonDataModel.NON_DISCOUNT_TIER_ID,
                    tierDiscountStr = "diskon 25%",
                    priceBeforeBenefit = 1100000.0,
                    priceAfterBenefit = 1035000.0,
                    products = listOf(
                        BmgmMiniCartVisitable.ProductUiModel(
                            productId = "111",
                            warehouseId = "12",
                            productName = "cyp - tetra",
                            productImage = "https://images.tokopedia.net/img/cache/500-square/VqbcmM/2022/9/21/f1237076-4c15-46cf-9359-388cf79d8bc9.jpg.webp?ect=4g",
                            finalPrice = 50000001.0,
                            quantity = 1
                        )
                    )
                )
            )
        )
    }
}