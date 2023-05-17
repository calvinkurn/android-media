package com.tokopedia.shop.campaign.data.mapper


import com.tokopedia.shop.campaign.data.response.GetPromoVoucherDetailResponse
import com.tokopedia.shop.campaign.domain.entity.PromoVoucherDetail
import javax.inject.Inject

class GetPromoVoucherDetailMapper @Inject constructor() {

    fun map(response: GetPromoVoucherDetailResponse): PromoVoucherDetail {
        val detail = response.detail
        return PromoVoucherDetail(
            detail.activePeriodDate,
            detail.buttonStr,
            detail.cta,
            detail.howToUse,
            detail.id,
            detail.imageUrl,
            detail.imageUrlMobile,
            detail.isDisabled,
            detail.isDisabledButton,
            detail.minimumUsage,
            detail.quota,
            detail.subTitle,
            detail.thumbnailUrl,
            detail.thumbnailUrlMobile,
            detail.title,
            detail.tnc
        )
    }
}
