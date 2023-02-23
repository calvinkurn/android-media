package com.tokopedia.tokopedianow.home.domain.model

data class GetCatalogCouponListResponse(
    val data: Data
) {
    data class Data(
        val tokopointsCatalogWithCouponList: TokopointsCatalogWithCouponList
    ) {
        data class TokopointsCatalogWithCouponList(
            val catalogWithCouponList: List<CatalogWithCoupon>,
            val resultStatus: ResultStatus
        ) {
            data class CatalogWithCoupon(
                val appLink: String,
                val baseCode: String,
                val buttonStr: String,
                val catalogType: Int,
                val couponCode: String,
                val cta: String,
                val ctaDesktop: String,
                val disableErrorMessage: String,
                val id: Int,
                val imageUrl: String,
                val imageUrlMobile: String,
                val isDisabled: Boolean,
                val isDisabledButton: Boolean,
                val minimumUsage: String,
                val minimumUsageLabel: String,
                val promoID: Int,
                val quota: Int,
                val slug: String,
                val smallImageUrl: String,
                val smallImageUrlMobile: String,
                val subTitle: String,
                val thumbnailUrl: String,
                val thumbnailUrlMobile: String,
                val title: String,
                val upperTextDesc: List<Any>,
                val url: String
            )
            data class ResultStatus(
                val code: String,
                val message: List<Any>,
                val status: String
            )
        }
    }
}
