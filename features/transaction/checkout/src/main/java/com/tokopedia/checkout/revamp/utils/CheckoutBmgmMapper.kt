package com.tokopedia.checkout.revamp.utils

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

object CheckoutBmgmMapper {

    private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2

    fun mapBmgmCommonDataModel(
        product: CheckoutProductModel,
        warehouseId: Long,
        shopId: String
    ): BmgmCommonDataModel {
        return BmgmCommonDataModel(
            offerId = product.bmgmOfferId,
            warehouseId = warehouseId,
            shopId = shopId,
            hasReachMaxDiscount = product.bmgmOfferStatus == OFFER_STATUS_HAS_REACH_MAX_DISC,
            tiersApplied = product.bmgmTierProductList.map { bmgmTier ->
                BmgmCommonDataModel.TierModel(
                    tierId = bmgmTier.tierId,
                    tierMessage = bmgmTier.tierMessage,
                    tierDiscountStr = bmgmTier.tierDiscountText,
                    priceBeforeBenefit = bmgmTier.priceBeforeBenefit,
                    priceAfterBenefit = bmgmTier.priceAfterBenefit,
                    products = bmgmTier.listProduct.map { bmgmProduct ->
                        val productPrice = if (bmgmProduct.wholesalePrice != 0.0) bmgmProduct.wholesalePrice else bmgmProduct.priceBeforeBenefit
                        BmgmCommonDataModel.ProductModel(
                            productId = bmgmProduct.productId,
                            warehouseId = bmgmProduct.warehouseId.toString(),
                            productName = bmgmProduct.productName,
                            productImage = bmgmProduct.imageUrl,
                            productPrice = productPrice,
                            quantity = bmgmProduct.quantity,
                            cartId = bmgmProduct.cartId
                        )
                    }
                )
            }
        )
    }
}
