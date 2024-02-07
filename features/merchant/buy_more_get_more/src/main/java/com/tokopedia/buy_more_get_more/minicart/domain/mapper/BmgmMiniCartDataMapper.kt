package com.tokopedia.buy_more_get_more.minicart.domain.mapper

import com.tokopedia.buy_more_get_more.common.OfferType
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.cartcommon.data.response.bmgm.BmGmTierProduct
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.common.data.response.minicartlist.CartDetail
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.minicart.common.data.response.minicartlist.ShoppingSummary
import com.tokopedia.minicart.common.data.response.minicartlist.WholesalePrice
import javax.inject.Inject
import kotlin.math.min

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

class BmgmMiniCartDataMapper @Inject constructor() {

    companion object {
        private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2
        const val BMGM_CART_DETAIL_TYPE = "bmgm"

        fun getFinalPrice(
            productQuantity: Int,
            wholesalePrice: List<WholesalePrice>,
            priceBeforeBenefit: Double
        ): Double {
            wholesalePrice.forEach {
                if (productQuantity >= it.qtyMin && productQuantity <= it.qtyMax) {
                    return it.prdPrc
                }
            }
            return priceBeforeBenefit
        }
    }

    fun mapToUiModel(data: MiniCartData): BmgmMiniCartDataUiModel {
        val shoppingSummary = data.data.shoppingSummary
        data.data.availableSection.availableGroup.forEach { group ->
            val cartString = group.cartString
            val detail = group.cartDetails.firstOrNull { detail ->
                val cartDetailInfo = detail.cartDetailInfo
                cartDetailInfo.cartDetailType.equals(BMGM_CART_DETAIL_TYPE, true) &&
                    cartDetailInfo.bmgmData.offerId != Long.ZERO
            }
            if (detail != null) {
                return getMiniCartData(
                    detail,
                    shoppingSummary,
                    cartString
                )
            }
        }
        return BmgmMiniCartDataUiModel()
    }

    private fun getMiniCartData(
        detail: CartDetail,
        shoppingSummary: ShoppingSummary,
        cartString: String
    ): BmgmMiniCartDataUiModel {
        val bmgmData = detail.cartDetailInfo.bmgmData
        val hasReachMaxDiscount = bmgmData.offerStatus == OFFER_STATUS_HAS_REACH_MAX_DISC
        val offerType = getOfferType(bmgmData.offerTypeId)
        return BmgmMiniCartDataUiModel(
            offerId = bmgmData.offerId,
            offerType = offerType,
            offerMessage = bmgmData.offerMessage,
            offerJsonData = bmgmData.offerJsonData,
            hasReachMaxDiscount = hasReachMaxDiscount,
            priceBeforeBenefit = shoppingSummary.totalOriginalValue,
            finalPrice = shoppingSummary.totalValue,
            tiers = getProductTiers(detail, offerType),
            isTierAchieved = bmgmData.isTierAchieved,
            products = detail.products,
            cartString = cartString
        )
    }

    private fun getProductTiers(
        detail: CartDetail,
        offerType: OfferType
    ): List<BmgmMiniCartVisitable> {
        val productListMap = detail.products.associateBy { it.productId }
        val data = detail.cartDetailInfo.bmgmData
        val isTierAchieved = data.isTierAchieved && offerType == OfferType.GIFT_WITH_PURCHASE
        return data.tierProductList.map { tier ->
            return@map BmgmMiniCartVisitable.TierUiModel(
                tierId = tier.tierId,
                tierMessage = tier.tierMessage,
                tierDiscountStr = tier.tierDiscountText,
                priceBeforeBenefit = tier.priceBeforeBenefit,
                priceAfterBenefit = tier.priceAfterBenefit,
                products = getProductList(
                    tier,
                    productListMap,
                    isTierAchieved
                ),
                productsBenefit = tier.productsBenefit.map { p ->
                    BmgmMiniCartVisitable.ProductUiModel(
                        productId = p.productId,
                        productName = p.productName,
                        productImage = p.productImage,
                        finalPrice = p.finalPrice,
                        quantity = p.quantity,
                        tierId = tier.tierId
                    )
                },
                benefitWording = tier.benefitWording,
                benefitCta = tier.actionWording
            )
        }
    }

    private fun getOfferType(offerTypeId: Long): OfferType {
        return when (offerTypeId) {
            OfferType.GIFT_WITH_PURCHASE.type -> OfferType.GIFT_WITH_PURCHASE
            else -> OfferType.PROGRESSIVE_DISCOUNT
        }
    }

    private fun getProductList(
        tier: BmGmTierProduct,
        productListMap: Map<String, Product>,
        isTierAchieved: Boolean
    ): List<BmgmMiniCartVisitable.ProductUiModel> {
        val products = mutableListOf<BmgmMiniCartVisitable.ProductUiModel>()
        tier.listProduct.forEach { tierProduct ->
            productListMap[tierProduct.productId]?.let { product ->
                val tmpProduct = BmgmMiniCartVisitable.ProductUiModel(
                    productId = product.productId,
                    warehouseId = tierProduct.warehouseId.toString(),
                    productName = product.productName,
                    productImage = product.productImage.imageSrc100Square,
                    cartId = product.cartId,
                    finalPrice = getFinalPrice(
                        product.productQuantity,
                        product.wholesalePrice,
                        tierProduct.priceBeforeBenefit
                    ),
                    quantity = tierProduct.quantity,
                    minQuantity = product.productMinOrder,
                    maxQuantity = if (product.productSwitchInvenage == 0) {
                        product.productMaxOrder
                    } else {
                        min(product.productMaxOrder, product.productInvenageValue)
                    },
                    tierId = tier.tierId,
                    ui = BmgmMiniCartVisitable.ProductUiModel.Ui(topMarginInDp = if (isTierAchieved) 12 else 0)
                )
                products.add(tmpProduct)
            }
        }
        return products.toList()
    }
}
