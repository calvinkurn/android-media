package com.tokopedia.mvc.data.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.INTEGER_MILLION
import com.tokopedia.kotlin.extensions.view.INTEGER_THOUSAND
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.mvc.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_FORMAT
import javax.inject.Inject

class GetCouponImagePreviewFacadeMapper @Inject constructor() {

    companion object {
        private const val FIRST_IMAGE_URL_INDEX = 0
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
    }

    fun mapToPreviewImageParam(
        param: GetCouponImagePreviewFacadeUseCase.GenerateCouponImageParam
    ) = param.toPreviewImageParam()

    fun GetCouponImagePreviewFacadeUseCase.GenerateCouponImageParam.toPreviewImageParam(): ImageGeneratorRemoteDataSource.PreviewImageParam {
        val startTime = voucherConfiguration.startPeriod.formatTo(DEFAULT_VIEW_FORMAT)
        val endTime = voucherConfiguration.endPeriod.formatTo(DEFAULT_VIEW_FORMAT)
        val firstProductImageUrl = topProductImageUrls.getOrNull(FIRST_IMAGE_URL_INDEX).orEmpty()
        val secondProductImageUrl = topProductImageUrls.getOrNull(SECOND_IMAGE_URL_INDEX).orEmpty()
        val thirdProductImageUrl = topProductImageUrls.getOrNull(THIRD_IMAGE_URL_INDEX).orEmpty()
        val secondProduct = secondProductImageUrl.ifEmpty { null }
        val thirdProduct = thirdProductImageUrl.ifEmpty { null }
        val productCount = topProductImageUrls.filter { it.isNotBlank() }.size

        return ImageGeneratorRemoteDataSource.PreviewImageParam(
            ImageGeneratorConstants.ImageGeneratorSourceId.MVC_PRODUCT,
            imageRatio.toFormattedImageRatio(),
            voucherConfiguration.getCouponVisibility(),
            voucherConfiguration.getBenefitType(),
            voucherConfiguration.getCashbackType(),
            voucherConfiguration.benefitPercent,
            voucherConfiguration.getNominalAmount(),
            voucherConfiguration.getSymbol(),
            shop.logo,
            MethodChecker.fromHtml(shop.name).toString(),
            voucherConfiguration.getCouponCode(isCreateMode, couponCodePrefix),
            startTime,
            endTime,
            productCount,
            firstProductImageUrl,
            secondProduct,
            thirdProduct,
            voucherConfiguration.getAudienceTarget()
        )
    }

    private fun VoucherConfiguration.getCouponCode(isCreateMode: Boolean, prefix: String): String {
        return if (isCreateMode && !isVoucherPublic) {
            prefix + voucherCode.uppercase()
        } else {
            voucherCode.uppercase()
        }
    }

    private fun VoucherConfiguration.getAudienceTarget(): String {
        return when(targetBuyer) {
            VoucherTargetBuyer.ALL_BUYER -> ImageGeneratorConstants.AUDIENCE_TARGET.ALL_USERS
            VoucherTargetBuyer.NEW_FOLLOWER -> ImageGeneratorConstants.AUDIENCE_TARGET.NEW_FOLLOWER
            VoucherTargetBuyer.NEW_BUYER -> ImageGeneratorConstants.AUDIENCE_TARGET.NEW_USER
            VoucherTargetBuyer.MEMBER -> ImageGeneratorConstants.AUDIENCE_TARGET.MEMBER
        }
    }

    private fun VoucherConfiguration.getNominalAmount(): Int {
        val amount = when {
            promoType == PromoType.FREE_SHIPPING -> benefitIdr
            promoType == PromoType.CASHBACK && benefitType == BenefitType.NOMINAL -> benefitIdr
            promoType == PromoType.CASHBACK && benefitType == BenefitType.PERCENTAGE -> benefitMax
            else -> benefitIdr
        }

        val formattedDiscountAmount : Float = when {
            amount < INTEGER_THOUSAND -> amount.toFloat()
            amount >= INTEGER_MILLION -> (amount / INTEGER_MILLION.toFloat())
            amount >= INTEGER_THOUSAND -> (amount / INTEGER_THOUSAND.toFloat())
            else -> amount.toFloat()
        }

        return formattedDiscountAmount.toInt()
    }

    private fun VoucherConfiguration.getSymbol(): String {
        return when {
            benefitIdr < INTEGER_THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            benefitIdr >= INTEGER_MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
            benefitIdr >= INTEGER_THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            else -> ""
        }
    }

    private fun VoucherConfiguration.getCashbackType(): String {
        return when {
            promoType == PromoType.FREE_SHIPPING -> "nominal"
            promoType == PromoType.CASHBACK && benefitType == BenefitType.NOMINAL -> "nominal"
            promoType == PromoType.CASHBACK && benefitType == BenefitType.PERCENTAGE -> "percentage"
            else -> ""
        }
    }

    private fun VoucherConfiguration.getBenefitType(): String {
        return when (promoType) {
            PromoType.DISCOUNT -> "discount"
            PromoType.CASHBACK -> "cashback"
            PromoType.FREE_SHIPPING -> "gratis-ongkir"
        }
    }

    private fun VoucherConfiguration.getCouponVisibility(): String {
        return when (isVoucherPublic) {
            true -> "public"
            else -> "private"
        }
    }

    private fun ImageRatio.toFormattedImageRatio(): String {
        return when (this) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }
    }
}
