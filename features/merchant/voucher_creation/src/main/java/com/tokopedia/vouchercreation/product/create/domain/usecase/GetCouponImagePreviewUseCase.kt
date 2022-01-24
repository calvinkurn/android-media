package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.CouponPreviewRequestParams
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
        imageRatio: ImageRatio,
        coupon: Coupon
    ): Int {
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val shop = shopDeferred.await()

        val generateImageDeferred  = scope.async { generateImage(coupon, imageRatio, shop) }
        generateImageDeferred.await()

        return -1
    }

    private fun generateImage(coupon: Coupon, imageRatio: ImageRatio, shop : ShopBasicDataResult) {
        val formattedImageRatio = when (imageRatio) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }

        val couponVisibility = when (coupon.information.target) {
            CouponInformation.Target.PUBLIC -> "public"
            CouponInformation.Target.SPECIAL -> "private"
        }

        val benefitType = when (coupon.settings.type) {
            CouponType.NONE -> ""
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "gratis-ongkir"
        }

        val cashbackType = when (coupon.settings.discountType) {
            DiscountType.NONE -> ""
            DiscountType.NOMINAL -> "nominal"
            DiscountType.PERCENTAGE -> "percentage"
        }

        val symbol = if (coupon.settings.discountAmount >= 1000) {
            "rb"
        } else {
            "jt"
        }

        val startTime = coupon.information.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = coupon.information.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        //TODO Upload image to public image
        val params = CouponPreviewRequestParams(
            formattedImageRatio,
            couponVisibility,
            benefitType,
            cashbackType,
            coupon.settings.discountPercentage,
            coupon.settings.discountAmount,
            symbol,
            shop.logo,
            shop.shopName,
            coupon.information.code,
            startTime,
            endTime,
            coupon.products.size,
            coupon.products[0].imageUrl,
            audienceTarget
        )
    }

}