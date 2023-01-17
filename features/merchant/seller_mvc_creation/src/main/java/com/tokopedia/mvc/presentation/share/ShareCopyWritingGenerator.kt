package com.tokopedia.mvc.presentation.share

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
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
import javax.inject.Inject
import com.tokopedia.mvc.R
import java.util.*

class ShareCopyWritingGenerator @Inject constructor(@ApplicationContext private val context: Context) {
    /**
     * Rules are defined on: https://www.figma.com/file/s7tCl4koVvbe8iO8hjYsId/%5BM%5D-MVC-Revamp-2022?node-id=3012%3A323755&t=HHF48HHxXpilbD9q-4
     */
    fun find(
        voucherType: VoucherServiceType,
        voucherTarget: VoucherTargetBuyer,
        promoType: PromoType,
        benefitType: BenefitType,
        param: Param
    ): String {
        return when {
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_cashback_nominal) //"1"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_cashback_percentage) //"2"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_free_shipping_nominal) //"3"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_free_shipping_percentage) //"4"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_discount_nominal) //"5"
            voucherType.isProductVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_discount_percentage) //"6"

            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_cashback_nominal) //"7"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_cashback_percentage) //"8"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_free_shipping_nominal)//"9"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_free_shipping_percentage)//"10"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_discount_nominal)//"11"
            voucherType.isShopVoucher() && voucherTarget.isAllBuyer() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_discount_percentage)//"12"

            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_cashback_nominal)//"13"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_cashback_percentage)//"14"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_free_shipping_nominal)//"15"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_free_shipping_percentage)//"16"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_discount_nominal)//"17"
            voucherType.isProductVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_product_nf_discount_percentage)//"18"

            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_cashback_nominal)//"19"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isCashback() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_cashback_percentage)//"20"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_free_shipping_nominal) //"21"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isFreeShipping() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_free_shipping_percentage)//"22"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isNominal() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_discount_nominal)//"23"
            voucherType.isShopVoucher() && voucherTarget.isNewFollower() && promoType.isDiscount() && benefitType.isPercentage() -> context.getString(R.string.smvc_outgoing_text_description_shop_nf_discount_percentage)//"24"

            else -> ""
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

}
