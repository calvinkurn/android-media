package com.tokopedia.mvc.data.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.INTEGER_MILLION
import com.tokopedia.kotlin.extensions.view.INTEGER_THOUSAND
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.mvc.domain.entity.CreateCouponProductParams
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.CreateCouponProductUseCase
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.utils.date.DateUtil.DEFAULT_VIEW_FORMAT
import com.tokopedia.utils.date.DateUtil.HH_MM
import com.tokopedia.utils.date.DateUtil.YYYY_MM_DD
import javax.inject.Inject

class GetCouponImagePreviewFacadeMapper @Inject constructor() {

    companion object {
        private const val FIRST_IMAGE_URL_INDEX = 0
        private const val SECOND_IMAGE_URL_INDEX = 1
        private const val THIRD_IMAGE_URL_INDEX = 2
        private const val FORMATTED_RATIO_SQUARE = "square"
        private const val FORMATTED_RATIO_VERTICAL = "vertical"
        private const val FORMATTED_RATIO_HORIZONTAL = "horizontal"
        private const val CREATION_SOURCE = "android-sellerapp"
        private const val SERVER_VALUE_TRUE = 1
        private const val SERVER_VALUE_FALSE = 0
        private const val DEFAULT_DELIMITER = ","
        private const val BENEFIT_TYPE_IDR = "idr"
        private const val BENEFIT_TYPE_PERCENT = "percent"
    }

    fun mapToPreviewImageParam(
        param: GetCouponImagePreviewFacadeUseCase.GenerateCouponImageParam
    ) = param.toPreviewImageParam()

    fun mapToCreateCouponProductParam(useCaseParam: CreateCouponProductUseCase.CreateCouponUseCaseParam): CreateCouponProductParams {
        with (useCaseParam) {
            val isPublic = if (voucherConfiguration.isVoucherPublic) SERVER_VALUE_TRUE else SERVER_VALUE_FALSE
            val isVoucherProduct = if (voucherConfiguration.isVoucherProduct) SERVER_VALUE_TRUE else SERVER_VALUE_FALSE
            val startDate = voucherConfiguration.startPeriod.formatTo(YYYY_MM_DD)
            val startHour = voucherConfiguration.startPeriod.formatTo(HH_MM)
            val endDate = voucherConfiguration.endPeriod.formatTo(YYYY_MM_DD)
            val endHour = voucherConfiguration.endPeriod.formatTo(HH_MM)
            val benefitType = when {
                voucherConfiguration.promoType == PromoType.FREE_SHIPPING -> BENEFIT_TYPE_IDR
                voucherConfiguration.promoType == PromoType.CASHBACK && voucherConfiguration.benefitType == BenefitType.NOMINAL -> BENEFIT_TYPE_IDR
                voucherConfiguration.promoType == PromoType.CASHBACK && voucherConfiguration.benefitType == BenefitType.PERCENTAGE -> BENEFIT_TYPE_PERCENT
                else -> BENEFIT_TYPE_IDR
            }

            val couponType = voucherConfiguration.getBenefitType()

            return CreateCouponProductParams(
                benefitIdr = voucherConfiguration.benefitIdr,
                benefitMax = voucherConfiguration.benefitMax,
                benefitPercent = voucherConfiguration.benefitPercent,
                benefitType = benefitType,
                code = voucherConfiguration.voucherCode,
                couponName = voucherConfiguration.voucherName,
                couponType = couponType,
                dateStart = startDate,
                dateEnd = endDate,
                hourStart = startHour,
                hourEnd = endHour,
                image = imageUrl,
                imageSquare = imageSquare,
                imagePortrait = imagePortrait,
                isPublic = isPublic,
                minPurchase = voucherConfiguration.minPurchase,
                quota = voucherConfiguration.quota,
                token = token,
                source = CREATION_SOURCE,
                targetBuyer = voucherConfiguration.targetBuyer.id,
                isLockToProduct = isVoucherProduct,
                productIds = couponProducts.joinToString(DEFAULT_DELIMITER) { it.parentProductId.toString() },
                warehouseId = warehouseId.toLongOrZero()
            )
        }
    }

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
