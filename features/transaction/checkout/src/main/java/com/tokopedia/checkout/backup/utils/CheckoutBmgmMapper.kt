package com.tokopedia.checkout.backup.utils

import com.tokopedia.checkout.backup.view.uimodel.CheckoutProductModel
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

object CheckoutBmgmMapper {

    private const val OFFER_STATUS_HAS_REACH_MAX_DISC = 2

    fun mapBmgmCommonDataModel(
        product: CheckoutProductModel,
        warehouseId: Long,
        shopId: String,
        title: String
    ): BmgmCommonDataModel {
        return BmgmCommonDataModel(
            bottomSheetTitle = title,
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
