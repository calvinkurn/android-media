package com.tokopedia.shop.campaign.data.mapper

import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.campaign.data.response.GetPromoVoucherListMapperResponse
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import javax.inject.Inject

class GetPromoVoucherListMapper @Inject constructor() {

    fun map(response: GetPromoVoucherListMapperResponse): List<ExclusiveLaunchVoucher> {
        return with(response) {
            tokopointsCatalogWithCouponList.catalogWithCouponList.map { voucher ->
                ExclusiveLaunchVoucher(
                    id = voucher.id,
                    voucherName = voucher.title,
                    minimumPurchase = voucher.minimumUsage.digitsOnly(),
                    remainingQuota = voucher.quota,
                    slug = voucher.slug,
                    isDisabledButton = voucher.isDisabledButton,
                    couponCode = voucher.couponCode,
                    buttonStr = voucher.buttonStr
                )
            }
        }
    }


    private fun String.digitsOnly() : Long {
        return this.getDigits()?.toLong().orZero()
    }
}
