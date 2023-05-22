package com.tokopedia.shop.campaign.data.mapper


import com.tokopedia.shop.campaign.data.response.GetPromoVoucherDetailResponse
import com.tokopedia.shop.campaign.domain.entity.PromoVoucherDetail
import javax.inject.Inject

class GetPromoVoucherDetailMapper @Inject constructor() {

    fun map(response: GetPromoVoucherDetailResponse): PromoVoucherDetail {
        val detail = response.hachikoCatalogDetail
        return PromoVoucherDetail(
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
            detail.pointsStr
        )
    }
}
