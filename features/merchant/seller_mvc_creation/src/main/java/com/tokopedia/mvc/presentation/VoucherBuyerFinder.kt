package com.tokopedia.mvc.presentation

import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherTarget
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.extension.isCashback
import com.tokopedia.mvc.util.extension.isDiscount
import com.tokopedia.mvc.util.extension.isFreeShipping
import com.tokopedia.mvc.util.extension.isPrivate
import com.tokopedia.mvc.util.extension.isProductVoucher
import com.tokopedia.mvc.util.extension.isPublic
import com.tokopedia.mvc.util.extension.isShopVoucher
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
}
