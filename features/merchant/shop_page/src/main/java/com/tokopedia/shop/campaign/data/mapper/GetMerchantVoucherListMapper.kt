package com.tokopedia.shop.campaign.data.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.campaign.data.response.GetMerchantVoucherListResponse
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import javax.inject.Inject

class GetMerchantVoucherListMapper @Inject constructor() {

    fun map(response: GetMerchantVoucherListResponse) : List<ExclusiveLaunchVoucher> {
        return with(response) {
            tokopointsCatalogMVCList.catalogList.map { voucher ->
                ExclusiveLaunchVoucher(
                    id = voucher.id.toLongOrZero(),
                    voucherName = voucher.title,
                    minimumPurchase = voucher.minimumUsageAmount,
                    remainingQuota = voucher.quotaLeft,
                    source = ExclusiveLaunchVoucher.VoucherSource.MERCHANT_CREATED
                )
            }
        }
    }

}
