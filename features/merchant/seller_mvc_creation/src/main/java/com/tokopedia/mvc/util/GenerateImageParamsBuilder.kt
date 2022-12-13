package com.tokopedia.mvc.util

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.mvc.domain.entity.GenerateImageProperty
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.extension.getIndexAtOrEmpty
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
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
        parentProductImageUrls: List<String>,
        shopLogo: String,
        shopName: String,
        voucher: UpdateVoucher
    ): GenerateImageProperty {
        val formattedShopName = MethodChecker.fromHtml(shopName).toString()
        val formattedImageRatio = when (imageRatio) {
            ImageRatio.SQUARE -> "square"
            ImageRatio.VERTICAL -> "vertical"
            ImageRatio.HORIZONTAL -> "horizontal"
        }

        val couponVisibility = if (voucher.isPublic) {
            ImageGeneratorConstants.VoucherVisibility.PUBLIC
        } else {
            ImageGeneratorConstants.VoucherVisibility.PRIVATE
        }

        val formattedBenefitType = when {
            voucher.voucherType == PromoType.FREE_SHIPPING -> ImageGeneratorConstants.CashbackType.NOMINAL
            voucher.voucherType == PromoType.CASHBACK && voucher.voucherBenefitType == BenefitType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
            voucher.voucherType == PromoType.CASHBACK && voucher.voucherBenefitType == BenefitType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
            else -> ImageGeneratorConstants.CashbackType.NOMINAL
        }

        // TODO verify them
        val amount = when {
            voucher.voucherType == PromoType.FREE_SHIPPING -> voucher.voucherNominalAmount
            voucher.voucherType == PromoType.CASHBACK && voucher.voucherBenefitType == BenefitType.NOMINAL -> voucher.voucherNominalAmount
            voucher.voucherType == PromoType.CASHBACK && voucher.voucherBenefitType == BenefitType.PERCENTAGE -> voucher.voucherNominalAmount
            else -> voucher.voucherNominalAmount
        }

        val symbol = when {
            amount < THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            amount >= MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
            amount >= THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            else -> ImageGeneratorConstants.VoucherNominalSymbol.RB
        }

        val formattedDiscountAmount: Float = when {
            amount < THOUSAND -> amount.toFloat()
            amount >= MILLION -> (amount / MILLION)
            amount >= THOUSAND -> (amount / THOUSAND)
            else -> amount.toFloat()
        }

        val nominalAmount = formattedDiscountAmount.toLong()

//        val startTime = couponInformation.period.startDate.parseTo(DateTimeUtils.DATE_FORMAT)
//        val endTime = couponInformation.period.endDate.parseTo(DateTimeUtils.DATE_FORMAT)

        val audienceTarget = when (voucher.audienceTarget) {
            VoucherTargetBuyer.ALL_BUYER -> ImageGeneratorConstants.AUDIENCE_TARGET.ALL_USERS
            VoucherTargetBuyer.NEW_FOLLOWER -> ImageGeneratorConstants.AUDIENCE_TARGET.NEW_FOLLOWER
            VoucherTargetBuyer.NEW_BUYER -> ImageGeneratorConstants.AUDIENCE_TARGET.NEW_USER
            VoucherTargetBuyer.MEMBER -> ImageGeneratorConstants.AUDIENCE_TARGET.MEMBER
        }

        return GenerateImageProperty(
            platform = formattedImageRatio,
            isPublic = couponVisibility,
            voucherBenefitType = formattedBenefitType,
            // TODO change this
            voucherCashbackPercentage = voucher.voucherDiscountPercentage,
            voucherNominalAmount = formattedDiscountAmount.toString(),
            voucherCashbackType = formattedBenefitType,
            voucherNominalSymbol = symbol,
            voucherDiscountType = formattedDiscountAmount.toString(),
            voucherDiscountPercentage = voucher.voucherDiscountPercentage,
            shopLogo = shopLogo,
            shopName = formattedShopName,
            voucherCode = voucher.voucherCode,
            // TODO change this
            voucherStartTime = voucher.voucherStartTime,
            voucherEndTime = voucher.voucherEndTime,
            productCount = voucher.productImageUrls.size.toString(),
            productImage1 = voucher.productImageUrls.getIndexAtOrEmpty(FIRST_IMAGE_URL_INDEX),
            productImage2 = voucher.productImageUrls.getIndexAtOrEmpty(SECOND_IMAGE_URL_INDEX),
            productImage3 = voucher.productImageUrls.getIndexAtOrEmpty(THIRD_IMAGE_URL_INDEX),
            audienceTarget = audienceTarget
        )
    }
}
