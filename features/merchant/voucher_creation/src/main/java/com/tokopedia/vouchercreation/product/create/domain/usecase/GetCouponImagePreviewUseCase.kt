package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.request.CouponPreviewRequestParams
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class GetCouponImagePreviewUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase
) {

    suspend fun execute(
        scope: CoroutineScope,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        productCount: Int,
        productImageUrl: String,
        imageRatio: ImageRatio
    ): Int {
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val shop = shopDeferred.await()

        val generateImageDeferred = scope.async {
            generateImage(
                couponInformation,
                couponSettings,
                productCount,
                productImageUrl,
                imageRatio,
                shop
            )
        }

        generateImageDeferred.await()

        return -1
    }

    private fun generateImage(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        productCount: Int,
        productImageUrl: String,
        imageRatio: ImageRatio,
        shop: ShopBasicDataResult
    ) {
        val formattedImageRatio = when (imageRatio) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }

        val couponVisibility = when (couponInformation.target) {
            CouponInformation.Target.PUBLIC -> "public"
            CouponInformation.Target.SPECIAL -> "private"
        }

        val benefitType = when (couponSettings.type) {
            CouponType.NONE -> ""
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "gratis-ongkir"
        }

        val cashbackType = when (couponSettings.discountType) {
            DiscountType.NONE -> ""
            DiscountType.NOMINAL -> "nominal"
            DiscountType.PERCENTAGE -> "percentage"
        }

        val symbol = if (couponSettings.discountAmount >= 1000) {
            "rb"
        } else {
            "jt"
        }

        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        //TODO Upload image to public image
        val params = CouponPreviewRequestParams(
            formattedImageRatio,
            couponVisibility,
            benefitType,
            cashbackType,
            couponSettings.discountPercentage,
            couponSettings.discountAmount,
            symbol,
            shop.logo,
            shop.shopName,
            couponInformation.code,
            startTime,
            endTime,
            productCount,
            productImageUrl,
            audienceTarget
        )
    }

}