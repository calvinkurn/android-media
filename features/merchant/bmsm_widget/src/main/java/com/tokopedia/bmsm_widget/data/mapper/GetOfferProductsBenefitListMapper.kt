package com.tokopedia.bmsm_widget.data.mapper

import com.tokopedia.bmsm_widget.data.response.GetOfferProductsBenefitListResponse
import com.tokopedia.bmsm_widget.domain.entity.TierGift
import javax.inject.Inject

class GetOfferProductsBenefitListMapper @Inject constructor() {
    fun map(response: GetOfferProductsBenefitListResponse): List<TierGift> {
        return response.getOfferProductsBenefitList.tierListGift.map {
            TierGift(
                tierId = it.tierId,
                tierName = it.tierName,
                maxBenefitQty = it.maxBenefitQty,
                isEligible = it.isEligible,
                tierMessage = it.tierMessage,
                products = it.products.map { product ->
                    TierGift.GiftProduct(
                        productId = product.productId,
                        warehouseId = product.warehouseId,
                        quantity = product.quantity,
                        stock = product.stock,
                        productName = product.productName,
                        productImageUrl = product.productCacheImageUrl,
                        originalPrice = product.originalPrice,
                        finalPrice = product.finalPrice,
                        isOutOfStock = product.isOos
                    )
                }
            )
        }
    }
}
