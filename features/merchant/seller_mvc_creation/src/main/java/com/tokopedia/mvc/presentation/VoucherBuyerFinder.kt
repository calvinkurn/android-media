package com.tokopedia.mvc.presentation

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import javax.inject.Inject

class VoucherBuyerFinder @Inject constructor() {

    fun findBuyerTarget(
        voucherType: VoucherServiceType,
        voucherTarget: VoucherTarget,
        promoType: PromoType
    ): List<VoucherTargetBuyer> {
        return when {
            voucherType.isShopVoucher() && voucherTarget.isPublic() && promoType.isCashback() -> listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)
            voucherType.isShopVoucher() && voucherTarget.isPublic() && promoType.isFreeShipping() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isShopVoucher() && voucherTarget.isPublic() && promoType.isDiscount() -> listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)
            voucherType.isShopVoucher() && voucherTarget.isPrivate() && promoType.isCashback() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isShopVoucher() && voucherTarget.isPrivate() && promoType.isFreeShipping() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isShopVoucher() && voucherTarget.isPrivate() && promoType.isDiscount() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isProductVoucher() && voucherTarget.isPublic() && promoType.isCashback() -> listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)
            voucherType.isProductVoucher() && voucherTarget.isPublic() && promoType.isFreeShipping() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isProductVoucher() && voucherTarget.isPublic() && promoType.isDiscount() -> listOf(VoucherTargetBuyer.ALL_BUYER, VoucherTargetBuyer.NEW_FOLLOWER)
            voucherType.isProductVoucher() && voucherTarget.isPrivate() && promoType.isCashback() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isProductVoucher() && voucherTarget.isPrivate() && promoType.isFreeShipping() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            voucherType.isProductVoucher() && voucherTarget.isPrivate() && promoType.isDiscount() -> listOf(VoucherTargetBuyer.ALL_BUYER)
            else -> emptyList()
        }
    }

    private fun VoucherServiceType.isShopVoucher(): Boolean {
        return this == VoucherServiceType.SHOP_VOUCHER
    }

    private fun VoucherServiceType.isProductVoucher(): Boolean {
        return this == VoucherServiceType.PRODUCT_VOUCHER
    }

    private fun VoucherTarget.isPrivate(): Boolean {
        return this == VoucherTarget.PRIVATE
    }

    private fun VoucherTarget.isPublic(): Boolean {
        return this == VoucherTarget.PUBLIC
    }

    private fun PromoType.isCashback(): Boolean {
        return this == PromoType.CASHBACK
    }

    private fun PromoType.isFreeShipping(): Boolean {
        return this == PromoType.FREE_SHIPPING
    }

    private fun PromoType.isDiscount(): Boolean {
        return this == PromoType.DISCOUNT
    }
}
