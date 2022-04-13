package com.tokopedia.vouchercreation.product.create.util

import com.tokopedia.vouchercreation.common.extension.getIndexAtOrEmpty
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import javax.inject.Inject

class GenerateImageParamsBuilder @Inject constructor() {

    companion object {
        private const val EMPTY_STRING = ""
        private const val THOUSAND = 1_000f
        private const val MILLION = 1_000_000f
        private const val FIRST_IMAGE_URL_INDEX = 0
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
    }

    fun build(
        imageRatio: ImageRatio,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        parentProductImageUrls: List<String>,
        shopLogo: String,
        shopName: String
    ): GenerateImageProperty {

        val formattedImageRatio = when (imageRatio) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }

        val couponVisibility = when (couponInformation.target) {
            CouponInformation.Target.PUBLIC -> "public"
            CouponInformation.Target.PRIVATE -> "private"
            CouponInformation.Target.NOT_SELECTED -> EMPTY_STRING
        }

        val benefitType = when (couponSettings.type) {
            CouponType.NONE -> EMPTY_STRING
            CouponType.CASHBACK -> "cashback"
            CouponType.FREE_SHIPPING -> "gratis-ongkir"
        }

        val cashbackType = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> "nominal"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> "nominal"
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> "percentage"
            else -> EMPTY_STRING
        }


        val amount = when {
            couponSettings.type == CouponType.FREE_SHIPPING -> couponSettings.discountAmount
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.NOMINAL -> couponSettings.discountAmount
            couponSettings.type == CouponType.CASHBACK && couponSettings.discountType == DiscountType.PERCENTAGE -> couponSettings.maxDiscount
            else -> couponSettings.discountAmount
        }

        val symbol = when {
            amount < THOUSAND -> "rb"
            amount >= MILLION -> "jt"
            amount >= THOUSAND -> "rb"
            else -> "rb"
        }

        val formattedDiscountAmount : Float = when {
            amount < THOUSAND -> amount.toFloat()
            amount >= MILLION -> (amount / MILLION)
            amount >= THOUSAND -> (amount / THOUSAND)
            else -> amount.toFloat()
        }

        val nominalAmount = formattedDiscountAmount.toInt()

        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        return GenerateImageProperty(
            formattedImageRatio,
            couponVisibility,
            benefitType,
            cashbackType,
            couponSettings.discountPercentage.toString(),
            nominalAmount.toString(),
            symbol,
            shopLogo,
            shopName,
            couponInformation.code,
            startTime,
            endTime,
            parentProductImageUrls.size.toString(),
            parentProductImageUrls.getIndexAtOrEmpty(FIRST_IMAGE_URL_INDEX),
            parentProductImageUrls.getIndexAtOrEmpty(SECOND_IMAGE_URL_INDEX),
            parentProductImageUrls.getIndexAtOrEmpty(THIRD_IMAGE_URL_INDEX),
            audienceTarget
        )
    }
}