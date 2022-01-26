package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.vouchercreation.common.extension.parseTo
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.product.create.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.vouchercreation.product.create.domain.entity.*
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.ShopBasicDataResult
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.ShopBasicDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import okhttp3.ResponseBody
import javax.inject.Inject

class GetCouponImagePreviewFacadeUseCase @Inject constructor(
    private val getShopBasicDataUseCase: ShopBasicDataUseCase,
    private val remoteDataSource: ImageGeneratorRemoteDataSource
) {

    companion object {
        private const val SOURCE_ID = "aaGBeS"
        private const val EMPTY_STRING = ""
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
    ): ResponseBody {
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

        return image
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
            CouponInformation.Target.SPECIAL -> "private"
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

        val symbol = if (couponSettings.discountAmount >= 1000) {
            "rb"
        } else {
            "jt"
        }

        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = "all-users"

        return remoteDataSource.previewImage(
            SOURCE_ID,
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
            firstProductImageUrl,
            secondProductImageUrl,
            thirdProductImageUrl,
            audienceTarget
        )
    }

}