package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.minicart.bmgm.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.minicart.common.data.response.minicartlist.MiniCartData
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import com.tokopedia.minicart.common.data.response.minicartlist.ShoppingSummary
import com.tokopedia.minicart.common.data.response.minicartlist.WholesalePrice
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmData
import com.tokopedia.purchase_platform.common.feature.bmgm.data.response.BmGmProduct
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
        val shoppingSummary = data.data.shoppingSummary
        data.data.availableSection.availableGroup.forEach { group ->
            val detail = group.cartDetails.firstOrNull { detail ->
                val cartDetailInfo = detail.cartDetailInfo
                cartDetailInfo.cartDetailType.equals(CART_DETAIL_TYPE, true) &&
                        cartDetailInfo.bmgmData.offerId != Long.ZERO
            }
            if (detail != null) {
                val productListMap = detail.products.associateBy { it.productId }
                return getMiniCartData(
                    detail.cartDetailInfo.bmgmData,
                    shoppingSummary,
                    productListMap
                )
            }
        }
        return BmgmMiniCartDataUiModel()
    }

    private fun getMiniCartData(
        bmgmData: BmGmData,
        shoppingSummary: ShoppingSummary,
        productListMap: Map<String, Product>
    ): BmgmMiniCartDataUiModel {
        val hasReachMaxDiscount = bmgmData.offerStatus == OFFER_STATUS_HAS_REACH_MAX_DISC
        return BmgmMiniCartDataUiModel(
            offerId = bmgmData.offerId,
            offerMessage = bmgmData.offerMessage,
            hasReachMaxDiscount = hasReachMaxDiscount,
            priceBeforeBenefit = shoppingSummary.totalOriginalValue,
            finalPrice = shoppingSummary.totalValue,
            tiersApplied = bmgmData.tierProductList.map { tier ->
                BmgmMiniCartVisitable.TierUiModel(
                    tierId = tier.tierId,
                    tierMessage = tier.tierMessage,
                    tierDiscountStr = tier.tierDiscountText,
                    priceBeforeBenefit = tier.priceBeforeBenefit,
                    priceAfterBenefit = tier.priceAfterBenefit,
                    products = getProductList(tier.listProduct, productListMap)
                )
            }
        )
    }

    private fun getProductList(
        tierProductList: List<BmGmProduct>,
        productListMap: Map<String, Product>
    ): List<BmgmMiniCartVisitable.ProductUiModel> {
        val products = mutableListOf<BmgmMiniCartVisitable.ProductUiModel>()
        tierProductList.forEach { tierProduct ->
            productListMap[tierProduct.productId]?.let { p ->
                val product = BmgmMiniCartVisitable.ProductUiModel(
                    productId = p.productId,
                    warehouseId = tierProduct.warehouseId.toString(),
                    productName = p.productName,
                    productImage = p.productImage.imageSrc100Square,
                    cartId = p.cartId,
                    finalPrice = getFinalPrice(
                        p.productQuantity,
                        p.wholesalePrice,
                        tierProduct.priceBeforeBenefit
                    ),
                    quantity = tierProduct.quantity
                )
                repeat(tierProduct.quantity) {
                    products.add(product)
                }
            }
        }
        return products
    }

    private fun getFinalPrice(
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