package com.tokopedia.shop.campaign.data.mapper


import com.tokopedia.shop.campaign.data.response.GetVoucherDetailResponse
import com.tokopedia.shop.campaign.domain.entity.VoucherDetail
import javax.inject.Inject

class GetVoucherDetailMapper @Inject constructor() {

    fun map(response: GetVoucherDetailResponse): VoucherDetail {
        val detail = response.hachikoCatalogDetail
        return VoucherDetail(
            detail.activePeriodDate,
            detail.buttonStr,
            detail.howToUse,
            detail.id,
            detail.imageUrlMobile,
            detail.isDisabledButton,
            detail.minimumUsage,
            detail.quota,
            detail.title,
            detail.tnc,
            detail.isGift,
            detail.pointsStr,
            detail.expired
        )
    }
}
