package com.tokopedia.checkout.revamp.utils

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.purchase_platform.common.feature.bmgm.data.uimodel.BmgmCommonDataModel

object CheckoutBmgmMapper {

    fun mapBmgmCommonDataModel(
        product: CheckoutProductModel
    ): BmgmCommonDataModel {
        return BmgmCommonDataModel(
            offerId = product.bmgmOfferId,
            offerName = product.bmgmOfferName,
            offerMessage = product.bmgmOfferMessage[0], // TODO: [Misael] ini update mapping array
            hasReachMaxDiscount = false, // TODO: [Misael] BE ETA 4 Sept, dummy false
            tiersApplied = product.bmgmTierProductGroup.map { bmgmTier ->
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
                            quantity = bmgmProduct.quantity
                        )
                    }
                )
            }
        )
    }
}
