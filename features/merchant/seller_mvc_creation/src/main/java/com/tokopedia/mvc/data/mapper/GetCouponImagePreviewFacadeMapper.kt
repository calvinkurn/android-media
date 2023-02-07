package com.tokopedia.mvc.data.mapper

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.INTEGER_MILLION
import com.tokopedia.kotlin.extensions.view.INTEGER_THOUSAND
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.mvc.data.request.GenerateImageParams
import com.tokopedia.mvc.data.source.ImageGeneratorRemoteDataSource
import com.tokopedia.mvc.domain.entity.CreateCouponProductParams
import com.tokopedia.mvc.domain.entity.UpdateCouponRequestParams
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.domain.usecase.CreateCouponProductUseCase
import com.tokopedia.mvc.domain.usecase.GenerateImageUseCase
import com.tokopedia.mvc.domain.usecase.GetCouponImagePreviewFacadeUseCase
import com.tokopedia.mvc.domain.usecase.UpdateCouponUseCase
import com.tokopedia.mvc.util.constant.Source
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
        private const val SERVER_VALUE_TRUE = 1
        private const val SERVER_VALUE_FALSE = 0
        private const val DEFAULT_DELIMITER = ","
        private const val BENEFIT_TYPE_IDR = "idr"
        private const val BENEFIT_TYPE_PERCENT = "percent"

        private const val COUPON_PRODUCT_PLATFORM = "platform"
        private const val COUPON_PRODUCT_IS_PUBLIC = "is_public"
        private const val COUPON_PRODUCT_VOUCHER_BENEFIT_TYPE = "voucher_benefit_type"
        private const val COUPON_PRODUCT_VOUCHER_CASHBACK_TYPE = "voucher_cashback_type"
        private const val COUPON_PRODUCT_VOUCHER_CASHBACK_PERCENTAGE = "voucher_cashback_percentage"
        private const val COUPON_PRODUCT_VOUCHER_NOMINAL_AMOUNT = "voucher_nominal_amount"
        private const val COUPON_PRODUCT_VOUCHER_NOMINAL_SYMBOL = "voucher_nominal_symbol"
        private const val COUPON_PRODUCT_VOUCHER_DISCOUNT_TYPE = "voucher_discount_type"
        private const val COUPON_PRODUCT_VOUCHER_DISCOUNT_PERCENTAGE = "voucher_discount_percentage"

        private const val COUPON_PRODUCT_SHOP_LOGO = "shop_logo"
        private const val COUPON_PRODUCT_SHOP_NAME = "shop_name"
        private const val COUPON_PRODUCT_VOUCHER_CODE = "voucher_code"
        private const val COUPON_PRODUCT_VOUCHER_START_TIME = "voucher_start_time"
        private const val COUPON_PRODUCT_VOUCHER_FINISH_TIME = "voucher_finish_time"
        private const val COUPON_PRODUCT_PRODUCT_COUNT = "product_count"
        private const val COUPON_PRODUCT_AUDIENCE_TARGET = "audience_target"

        private const val COUPON_PRODUCT_FIRST_PRODUCT_IMAGE= "product_image_1"
        private const val COUPON_PRODUCT_SECOND_PRODUCT_IMAGE = "product_image_2"
        private const val COUPON_PRODUCT_THIRD_PRODUCT_IMAGE = "product_image_3"
    }

    fun mapToPreviewUrlImageParam(param: GetCouponImagePreviewFacadeUseCase.GenerateCouponImageParam): HashMap<String, Any> {
        val imageParams = param.toPreviewImageParam()
        val requestParams = arrayListOf(
            GenerateImageParams(COUPON_PRODUCT_PLATFORM, imageParams.platform),
            GenerateImageParams(COUPON_PRODUCT_IS_PUBLIC, imageParams.isPublic),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_BENEFIT_TYPE, imageParams.voucherBenefitType),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_CASHBACK_TYPE, imageParams.voucherCashbackType),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_CASHBACK_PERCENTAGE, imageParams.voucherCashbackPercentage.toString()),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_DISCOUNT_TYPE, imageParams.voucherCashbackType),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_DISCOUNT_PERCENTAGE, imageParams.voucherCashbackPercentage.toString()),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_NOMINAL_AMOUNT, imageParams.voucherNominalAmount.toString()),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_NOMINAL_SYMBOL, imageParams.voucherNominalSymbol),
            GenerateImageParams(COUPON_PRODUCT_SHOP_LOGO, imageParams.shopLogo),
            GenerateImageParams(COUPON_PRODUCT_SHOP_NAME, imageParams.shopName),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_CODE, imageParams.voucherCode),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_START_TIME, imageParams.voucherStartTime),
            GenerateImageParams(COUPON_PRODUCT_VOUCHER_FINISH_TIME, imageParams.voucherFinishTime),
            GenerateImageParams(COUPON_PRODUCT_PRODUCT_COUNT, imageParams.productCount.toString()),
            GenerateImageParams(COUPON_PRODUCT_AUDIENCE_TARGET, imageParams.audienceTarget)
        )

        if (param.topProductImageUrls.isNotEmpty()) {
            requestParams.add(GenerateImageParams(COUPON_PRODUCT_FIRST_PRODUCT_IMAGE, imageParams.firstProductImageUrl))
        }

        if (param.topProductImageUrls.size >= SECOND_IMAGE_URL_INDEX) {
            requestParams.add(GenerateImageParams(COUPON_PRODUCT_SECOND_PRODUCT_IMAGE, imageParams.secondProductImageUrl.orEmpty()))
        }

        if (param.topProductImageUrls.size >= THIRD_IMAGE_URL_INDEX) {
            requestParams.add(GenerateImageParams(COUPON_PRODUCT_THIRD_PRODUCT_IMAGE, imageParams.thirdProductImageUrl.orEmpty()))
        }

        return GenerateImageUseCase.createParam(imageParams.sourceId, requestParams)
    }

    fun mapToPreviewImageParam(
        param: GetCouponImagePreviewFacadeUseCase.GenerateCouponImageParam
    ) = param.toPreviewImageParam()

    fun mapToUpdateCouponProductParam(
        useCaseParam: UpdateCouponUseCase.UpdateCouponUseCaseParam
    ): UpdateCouponRequestParams {
        with(useCaseParam) {
            val isPublic = if (voucherConfiguration.isVoucherPublic) SERVER_VALUE_TRUE else SERVER_VALUE_FALSE
            val isVoucherProduct = if (voucherConfiguration.isVoucherProduct) SERVER_VALUE_TRUE else SERVER_VALUE_FALSE
            val startDate = voucherConfiguration.startPeriod.formatTo(YYYY_MM_DD)
            val startHour = voucherConfiguration.startPeriod.formatTo(HH_MM)
            val endDate = voucherConfiguration.endPeriod.formatTo(YYYY_MM_DD)
            val endHour = voucherConfiguration.endPeriod.formatTo(HH_MM)
            val benefitType = voucherConfiguration.getBenefitTypeConst()

            return UpdateCouponRequestParams(
                voucherId = couponId,
                benefitIdr = voucherConfiguration.benefitIdr,
                benefitMax = voucherConfiguration.benefitMax,
                benefitPercent = voucherConfiguration.benefitPercent,
                benefitType = benefitType,
                code = voucherConfiguration.voucherCode,
                couponName = voucherConfiguration.voucherName,
                couponType = voucherConfiguration.promoType.text,
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
                source = Source.SOURCE,
                targetBuyer = voucherConfiguration.targetBuyer.id,
                isLockToProduct = isVoucherProduct,
                productIds = couponProducts.joinToString(DEFAULT_DELIMITER) { it.parentProductId.toString() },
                warehouseId = warehouseId.toLongOrZero()
            )
        }
    }

    fun mapToCreateCouponProductParam(
        useCaseParam: CreateCouponProductUseCase.CreateCouponUseCaseParam
    ): CreateCouponProductParams {
        with (useCaseParam) {
            val isPublic = if (voucherConfiguration.isVoucherPublic) SERVER_VALUE_TRUE else SERVER_VALUE_FALSE
            val isVoucherProduct = if (voucherConfiguration.isVoucherProduct) SERVER_VALUE_TRUE else SERVER_VALUE_FALSE
            val startDate = voucherConfiguration.startPeriod.formatTo(YYYY_MM_DD)
            val startHour = voucherConfiguration.startPeriod.formatTo(HH_MM)
            val endDate = voucherConfiguration.endPeriod.formatTo(YYYY_MM_DD)
            val endHour = voucherConfiguration.endPeriod.formatTo(HH_MM)
            val benefitType = voucherConfiguration.getBenefitTypeConst()

            return CreateCouponProductParams(
                benefitIdr = voucherConfiguration.benefitIdr,
                benefitMax = voucherConfiguration.benefitMax,
                benefitPercent = voucherConfiguration.benefitPercent,
                benefitType = benefitType,
                code = voucherConfiguration.voucherCode,
                couponName = voucherConfiguration.voucherName,
                couponType = voucherConfiguration.promoType.text,
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
                source = Source.SOURCE,
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
            sourceId = ImageGeneratorConstants.ImageGeneratorSourceId.MVC_PRODUCT,
            platform = imageRatio.toFormattedImageRatio(),
            isPublic = voucherConfiguration.getCouponVisibility(),
            voucherBenefitType = voucherConfiguration.getBenefitType(),
            voucherCashbackType = voucherConfiguration.getCashbackType(),
            voucherCashbackPercentage = voucherConfiguration.benefitPercent,
            voucherNominalAmount = voucherConfiguration.getNominalAmount(),
            voucherNominalSymbol = voucherConfiguration.getSymbol(),
            voucherDiscountType = voucherConfiguration.getCashbackType(),
            voucherDiscountPercentage = voucherConfiguration.benefitPercent,
            shopLogo = shop.logo,
            shopName = MethodChecker.fromHtml(shop.name).toString(),
            voucherCode = voucherConfiguration.getCouponCode(isCreateMode, couponCodePrefix),
            voucherStartTime = startTime,
            voucherFinishTime = endTime,
            productCount = productCount,
            firstProductImageUrl = firstProductImageUrl,
            secondProductImageUrl = secondProduct,
            thirdProductImageUrl = thirdProduct,
            audienceTarget = voucherConfiguration.getAudienceTarget()
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
            (promoType == PromoType.CASHBACK || promoType == PromoType.DISCOUNT)
                && benefitType == BenefitType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
            (promoType == PromoType.CASHBACK || promoType == PromoType.DISCOUNT)
                && benefitType == BenefitType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
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

    private fun VoucherConfiguration.getBenefitTypeConst() = when {
        promoType == PromoType.FREE_SHIPPING -> BENEFIT_TYPE_IDR
        promoType == PromoType.CASHBACK && benefitType == BenefitType.NOMINAL -> BENEFIT_TYPE_IDR
        promoType == PromoType.CASHBACK && benefitType == BenefitType.PERCENTAGE -> BENEFIT_TYPE_PERCENT
        promoType == PromoType.DISCOUNT && benefitType == BenefitType.NOMINAL -> BENEFIT_TYPE_IDR
        promoType == PromoType.DISCOUNT && benefitType == BenefitType.PERCENTAGE -> BENEFIT_TYPE_PERCENT
        else -> BENEFIT_TYPE_IDR
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
