package com.tokopedia.shop.campaign.data.mapper

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
                    benefitMax = 1,
                    minimumPurchase = 1,
                    remainingQuota = voucher.quota,
                    source = ExclusiveLaunchVoucher.VoucherSource.PROMO
                )
            }
        }
    }

}
