package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmData
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmProductTier
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 08/08/23.
 */

class BmgmMiniCartDataMapper @Inject constructor() {

    companion object {
        private const val CART_DETAIL_TYPE = "bmgm"
        private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2
    }

    fun mapToUiModel(data: MiniCartData): BmgmMiniCartDataUiModel {
        var bmgm: BmGmData? = null
        val productMap = mutableMapOf<String, Product>()
        val shoppingSummary = data.data.shoppingSummary
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
                priceBeforeBenefit = shoppingSummary.totalOriginalValue,
                finalPrice = shoppingSummary.totalValue,
                totalDiscount = it.totalDiscount,
                tiersApplied = it.tiersApplied.map { tier ->
                    BmgmMiniCartVisitable.TierUiModel(
                        tierId = tier.tierId,
                        tierMessage = tier.tierMessage,
                        tierDiscountStr = tier.tierDiscountText,
                        priceBeforeBenefit = tier.priceBeforeBenefit,
                        priceAfterBenefit = tier.priceAfterBenefit,
                        products = getProductList(tier.listProduct, productMap)
                    )
                }
            )
        }
        return BmgmMiniCartDataUiModel()
    }

    private fun getProductList(
        tierProductList: List<BmGmProductTier>,
        productMap: MutableMap<String, Product>
    ): List<BmgmMiniCartVisitable.ProductUiModel> {
        val products = mutableListOf<BmgmMiniCartVisitable.ProductUiModel>()
        tierProductList.forEach { tierProduct ->
            productMap[tierProduct.productId]?.let { p ->
                val product = BmgmMiniCartVisitable.ProductUiModel(
                    productId = p.productId,
                    warehouseId = tierProduct.warehouseId.toString(),
                    productName = p.productName,
                    productImage = p.productImage.imageSrc100Square,
                    cartId = p.cartId,
                    finalPrice = tierProduct.priceBeforeBenefit,
                    quantity = tierProduct.qty
                )
                repeat(tierProduct.qty) {
                    products.add(product)
                }
            }
        }
        return products
    }
}