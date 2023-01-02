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
        private const val FORMATTED_RATIO_SQUARE = "square"
        private const val FORMATTED_RATIO_VERTICAL = "vertical"
        private const val FORMATTED_RATIO_HORIZONTAL = "horizontal"
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

        val formattedDiscountAmount = when {
            amount < INTEGER_THOUSAND -> amount.toDouble()
            else -> {
                val exp = (Math.log(amount.toDouble()) / Math.log(INTEGER_THOUSAND.toDouble())).toInt()
                amount.toDouble() / Math.pow(INTEGER_THOUSAND.toDouble(), exp.toDouble())
            }
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
            promoType == PromoType.FREE_SHIPPING -> ImageGeneratorConstants.CashbackType.NOMINAL
            promoType == PromoType.CASHBACK && benefitType == BenefitType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
            promoType == PromoType.CASHBACK && benefitType == BenefitType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
            else -> ""
        }
    }

    private fun VoucherConfiguration.getBenefitType(): String {
        return when (promoType) {
            PromoType.DISCOUNT -> ImageGeneratorConstants.VoucherBenefitType.DISCOUNT
            PromoType.CASHBACK -> ImageGeneratorConstants.VoucherBenefitType.CASHBACK
            PromoType.FREE_SHIPPING -> ImageGeneratorConstants.VoucherBenefitType.GRATIS_ONGKIR
        }
    }

    private fun VoucherConfiguration.getCouponVisibility(): String {
        return when (isVoucherPublic) {
            true -> ImageGeneratorConstants.VoucherVisibility.PUBLIC
            else -> ImageGeneratorConstants.VoucherVisibility.PRIVATE
        }
    }

    private fun ImageRatio.toFormattedImageRatio(): String {
        return when (this) {
            ImageRatio.SQUARE -> FORMATTED_RATIO_SQUARE
            ImageRatio.VERTICAL -> FORMATTED_RATIO_VERTICAL
            ImageRatio.HORIZONTAL -> FORMATTED_RATIO_HORIZONTAL
        }
    }
}
