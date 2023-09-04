package com.tokopedia.checkout.revamp.utils

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

object CheckoutBmgmMapper {

    private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2

    fun mapBmgmCommonDataModel(
        product: CheckoutProductModel
    ): BmgmCommonDataModel {
        return BmgmCommonDataModel(
            offerId = product.bmgmOfferId,
            offerName = product.bmgmOfferName,
            hasReachMaxDiscount = product.bmgmOfferStatus == OFFER_STATUS_HAS_REACH_MAX_DISC,
            tiersApplied = product.bmgmTierProductList.map { bmgmTier ->
                BmgmCommonDataModel.TierModel(
                    tierId = bmgmTier.tierId,
                    tierMessage = bmgmTier.tierMessage,
                    tierDiscountStr = bmgmTier.tierDiscountText,
                    priceBeforeBenefit = bmgmTier.priceBeforeBenefit,
                    priceAfterBenefit = bmgmTier.priceAfterBenefit,
                    products = bmgmTier.listProduct.map { bmgmProduct ->
                        BmgmCommonDataModel.ProductModel(
                            productId = bmgmProduct.productId,
                            warehouseId = bmgmProduct.warehouseId.toString(),
                            productName = product.name,
                            productImage = product.imageUrl,
                            productPrice = bmgmProduct.priceBeforeBenefit,
                            quantity = bmgmProduct.quantity,
                            cartId = bmgmProduct.cartId
                        )
                    }
                )
            }
        )
    }
}
