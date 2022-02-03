package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.common.consts.ImageGeneratorConstant
import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import javax.inject.Inject

class GetCouponImagePreviewFacadeUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val remoteDataSource: ImageGeneratorRemoteDataSource
) {

    companion object {
        private const val EMPTY_STRING = ""
        private const val THOUSAND  = 1_000f
        private const val MILLION = 1_000_000f
    }

    suspend fun execute(
        scope: CoroutineScope,
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        productCount: Int,
        firstProductImageUrl: String,
        secondProductImageUrl: String,
        thirdProductImageUrl: String,
        imageRatio: ImageRatio
    ): ByteArray {
        val shopDeferred = scope.async { getShopBasicDataUseCase.executeOnBackground() }
        val shop = shopDeferred.await()

        val generateImageDeferred = scope.async {
            generateImage(
                couponInformation,
                couponSettings,
                productCount,
                firstProductImageUrl,
                secondProductImageUrl,
                thirdProductImageUrl,
                imageRatio,
                shop
            )
        }

        val image = generateImageDeferred.await()

        val imageByteArray = withContext(scope.coroutineContext) { image.bytes() }

        return imageByteArray
    }

    private suspend fun generateImage(
        couponInformation: CouponInformation,
        couponSettings: CouponSettings,
        productCount: Int,
        firstProductImageUrl: String,
        secondProductImageUrl: String,
        thirdProductImageUrl: String,
        imageRatio: ImageRatio,
        shop: ShopBasicDataResult
    ): ResponseBody {
        val formattedImageRatio = when (imageRatio) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }

        val couponVisibility = when (couponInformation.target) {
            CouponInformation.Target.PUBLIC -> "public"
            CouponInformation.Target.PRIVATE -> "private"
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

        val symbol = when {
            couponSettings.discountAmount < THOUSAND -> EMPTY_STRING
            couponSettings.discountAmount >= MILLION -> "jt"
            couponSettings.discountAmount >= THOUSAND -> "rb"
            else -> EMPTY_STRING
        }

        val formattedDiscountAmount : Float = when {
            couponSettings.discountAmount < THOUSAND -> couponSettings.discountAmount.toFloat()
            couponSettings.discountAmount >= MILLION -> (couponSettings.discountAmount / MILLION)
            couponSettings.discountAmount >= THOUSAND -> (couponSettings.discountAmount / THOUSAND)
            else -> couponSettings.discountAmount.toFloat()
        }

        val nominalAmount = if (isInteger(formattedDiscountAmount)) {
            formattedDiscountAmount.toInt()
        } else {
            formattedDiscountAmount
        }

        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        return remoteDataSource.previewImage(
            ImageGeneratorConstant.IMAGE_TEMPLATE_COUPON_PRODUCT_SOURCE_ID,
            formattedImageRatio,
            couponVisibility,
            benefitType,
            cashbackType,
            couponSettings.discountPercentage,
            nominalAmount,
            symbol,
            shop.logo,
            shop.shopName,
            couponInformation.code,
            startTime,
            endTime,
            productCount,
            firstProductImageUrl,
            secondProductImageUrl,
            thirdProductImageUrl,
            audienceTarget
        )
    }

    private fun isInteger(number : Float) : Boolean {
        return number % 1 == 0.0f
    }

}