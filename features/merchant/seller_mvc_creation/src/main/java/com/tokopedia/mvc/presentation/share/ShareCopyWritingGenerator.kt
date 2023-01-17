package com.tokopedia.mvc.presentation.share

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.mvc.R
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherServiceType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.util.extension.isAllBuyer
import com.tokopedia.mvc.util.extension.isCashback
import com.tokopedia.mvc.util.extension.isDiscount
import com.tokopedia.mvc.util.extension.isFreeShipping
import com.tokopedia.mvc.util.extension.isNewFollower
import com.tokopedia.mvc.util.extension.isNominal
import com.tokopedia.mvc.util.extension.isPercentage
import com.tokopedia.mvc.util.extension.isProductVoucher
import com.tokopedia.mvc.util.extension.isShopVoucher
import com.tokopedia.mvc.util.formatTo
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import java.util.*
import javax.inject.Inject

class ShareCopyWritingGenerator @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val THOUSAND = 1_000f
        private const val MILLION = 1_000_000f
    }

    /**
     * Rules are defined on:
     * https://www.figma.com/file/s7tCl4koVvbe8iO8hjYsId/%5BM%5D-MVC-Revamp-2022?node-id=3012%3A323755&t=HHF48HHxXpilbD9q-4
     */
    fun findOutgoingDescription(
        isProductVoucher: Boolean,
        voucherTarget: VoucherTargetBuyer,
        promoType: PromoType,
        benefitType: BenefitType,
        param: Param
    ): String {
        val voucherType = if (isProductVoucher) VoucherServiceType.PRODUCT_VOUCHER else VoucherServiceType.SHOP_VOUCHER
        val amount = param.discountAmount.amount()
        val amountMax = param.discountAmountMax.amount()
        val symbol = param.discountAmount.symbol()
        val discountPercentage = param.discountPercentage

        val endDate = param.voucherEndDate.formatTo(DateConstant.DATE_YEAR_PRECISION)
        val endHour = param.voucherEndDate.formatTo(DateConstant.TIME_MINUTE_PRECISION)

        val fallbackCopyWriting = if (voucherType.isProductVoucher()) {
            context.getString(R.string.smvc_share_component_outgoing_text_description_product_voucher)
        } else {
            context.getString(R.string.smvc_share_component_outgoing_text_description_shop_voucher)
        }

        return when {
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_cashback_nominal, amount, symbol, param.shopName, endDate, endHour) //"1"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_cashback_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour) //"2"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_free_shipping_nominal, amount, symbol, param.shopName, endDate, endHour) //"3"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_free_shipping_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour) //"4"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_discount_nominal, amount, symbol,param.shopName, endDate, endHour) //"5"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_discount_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour) //"6"

            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_cashback_nominal, amount, symbol,param.shopName, endDate, endHour) //"7"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_cashback_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour) //"8"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_free_shipping_nominal, amount, symbol,param.shopName, endDate, endHour)//"9"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_free_shipping_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"10"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_discount_nominal, amount, symbol, param.shopName, endDate, endHour)//"11"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_discount_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"12"

            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_cashback_nominal, amount, symbol, param.shopName, endDate, endHour)//"13"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_cashback_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"14"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_free_shipping_nominal, amount, symbol, param.shopName, endDate, endHour)//"15"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_free_shipping_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"16"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_discount_nominal, amount, symbol, param.shopName, endDate, endHour)//"17"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_discount_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"18"

            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_cashback_nominal, amount, symbol, param.shopName, endDate, endHour)//"19"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_cashback_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"20"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_free_shipping_nominal, amount, symbol, param.shopName, endDate, endHour) //"21"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_free_shipping_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"22"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_discount_nominal,amount, symbol,  param.shopName, endDate, endHour)//"23"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_discount_percentage, discountPercentage, amountMax, symbol, param.shopName, endDate, endHour)//"24"

            else -> fallbackCopyWriting
        }
    }

    data class Param(
        val voucherStartDate: Date,
        val voucherEndDate: Date,
        val shopName: String,
        val discountAmount: Long,
        val discountAmountMax: Long,
        val discountPercentage: Int
    )

    private fun Long.amount(): Long {
        return when {
            this < THOUSAND -> this
            this >= MILLION -> (this / MILLION).toLong()
            this >= THOUSAND -> (this / THOUSAND).toLong()
            else -> this
        }
    }

    private fun Long.symbol(): String {
        return when {
            this < THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            this >= MILLION -> ImageGeneratorConstants.VoucherNominalSymbol.JT
            this >= THOUSAND -> ImageGeneratorConstants.VoucherNominalSymbol.RB
            else -> ImageGeneratorConstants.VoucherNominalSymbol.RB
        }
    }


}
