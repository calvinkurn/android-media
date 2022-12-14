package com.tokopedia.mvc.util

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.mvc.domain.entity.GenerateImageProperty
import com.tokopedia.mvc.domain.entity.UpdateVoucher
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.ImageRatio
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.DateTimeUtils.DATE_FORMAT
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

        val voucherType = PromoType.mapToString(voucher.type)

        val formattedBenefitType = when {
            voucherType == PromoType.FREE_SHIPPING.text -> ImageGeneratorConstants.CashbackType.NOMINAL
            voucherType == PromoType.CASHBACK.text && voucher.discountTypeFormatted == ImageGeneratorConstants.CashbackType.NOMINAL -> ImageGeneratorConstants.CashbackType.NOMINAL
            voucherType == PromoType.CASHBACK.text && voucher.discountTypeFormatted == ImageGeneratorConstants.CashbackType.PERCENTAGE -> ImageGeneratorConstants.CashbackType.PERCENTAGE
            else -> ImageGeneratorConstants.CashbackType.NOMINAL
        }

        val amount = when {
            voucherType == PromoType.FREE_SHIPPING.text -> voucher.discountAmt
            voucherType == PromoType.CASHBACK.text && voucher.voucherBenefitType == BenefitType.NOMINAL -> voucher.discountAmt
            voucherType == PromoType.CASHBACK.text && voucher.voucherBenefitType == BenefitType.PERCENTAGE -> voucher.discountAmtMax
            else -> voucher.discountAmt
        }

        val symbol = when {
            amount < THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            amount >= MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
            amount >= THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            else -> ImageGeneratorConstants.VoucherNominalSymbol.RB
        }

        val formattedDiscountAmount: Long = when {
            amount < THOUSAND -> amount.toLong()
            amount >= MILLION -> (amount / MILLION).toLong()
            amount >= THOUSAND -> (amount / THOUSAND).toLong()
            else -> amount.toLong()
        }

        val startTime = convertFormatDate(
            voucher.startTime,
            DateTimeUtils.DASH_DATE_FORMAT,
            DATE_FORMAT
        )
        val endTime = convertFormatDate(
            voucher.finishTime,
            DateTimeUtils.DASH_DATE_FORMAT,
            DATE_FORMAT
        )
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
            voucherCashbackPercentage = voucher.discountAmt.toString(),
            voucherNominalAmount = formattedDiscountAmount.toString(),
            voucherCashbackType = formattedBenefitType,
            voucherNominalSymbol = symbol,
            voucherDiscountType = formattedBenefitType,
            voucherDiscountPercentage = voucher.discountAmt.toString(),
            shopLogo = shopLogo,
            shopName = formattedShopName,
            voucherCode = voucher.voucherCode,
            voucherStartDate = startTime,
            voucherEndDate = endTime,
            productCount = parentProductImageUrls.size.toString(),
            productImage1 = parentProductImageUrls.getIndexAtOrEmpty(FIRST_IMAGE_URL_INDEX),
            productImage2 = parentProductImageUrls.getIndexAtOrEmpty(SECOND_IMAGE_URL_INDEX),
            productImage3 = parentProductImageUrls.getIndexAtOrEmpty(THIRD_IMAGE_URL_INDEX),
            audienceTarget = audienceTarget
        )
    }
}
