package com.tokopedia.digital_checkout.data.response.getcart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.atc.data.response.FintechProduct

/**
 * @author by jessica on 08/01/21
 */

data class RechargeGetCart(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("product_id")
        @Expose
        val productId: String = "",

        @SerializedName("user_id")
        @Expose
        val userId: String = "",

        @SerializedName("client_number")
        @Expose
        val clientNumber: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("category_name")
        @Expose
        val categoryName: String = "",

        @SerializedName("operator_name")
        @Expose
        val operatorName: String = "",

        @SerializedName("icon")
        @Expose
        val icon: String = "",

        @SerializedName("price_text")
        @Expose
        val priceText: String = "",

        @SerializedName("price")
        @Expose
        val price: Double = 0.0,

        @SerializedName("is_instant_checkout")
        @Expose
        val isInstantCheckout: Boolean = false,

        @SerializedName("is_otp_required")
        @Expose
        val isOtpRequired: Boolean = false,

        @SerializedName("sms_state")
        @Expose
        val sms_state: String = "",

        @SerializedName("voucher")
        @Expose
        val voucher: String = "",

        @SerializedName("is_open_payment")
        @Expose
        val isOpenPayment: Boolean = false,

        @SerializedName("open_payment_config")
        @Expose
        val openPaymentConfig: OpenPaymentConfig = OpenPaymentConfig(),

        @SerializedName("main_info")
        @Expose
        val mainnInfo: List<Attribute> = listOf(),

        @SerializedName("additional_info")
        @Expose
        val additionalInfo: List<AdditionalInfo> = listOf(),

        @SerializedName("enable_voucher")
        @Expose
        val enableVoucher: Boolean = false,

        @SerializedName("is_coupon_active")
        @Expose
        val isCouponActive: Boolean = false,

        @SerializedName("auto_apply")
        @Expose
        val autoApply: AutoApplyVoucher = AutoApplyVoucher(),

        @SerializedName("default_promo")
        @Expose
        val defaultPromo: String = "",

        @SerializedName("cross_selling_type")
        @Expose
        val crossSellingType: Int = 0,

        @SerializedName("cross_selling_config")
        @Expose
        val crossSellingConfig: CrossSellingConfig = CrossSellingConfig(),

        @SerializedName("pop_up")
        @Expose
        val popUp: PopUpData = PopUpData(),

        @SerializedName("fintech_products")
        @Expose
        val fintechProduct: List<FintechProduct> = listOf<FintechProduct>(),

        @SerializedName("atc_source")
        @Expose
        val atcSource: String = "",

        @SerializedName("admin_fee_text")
        @Expose
        val adminFeeText: String = "",

        @SerializedName("admin_fee")
        @Expose
        val adminFee: Int = 0
) {
    data class Response(
            @SerializedName("rechargeGetCart")
            @Expose
            val response: RechargeGetCart = RechargeGetCart()
    )

    data class OpenPaymentConfig(
            @SerializedName("min_payment")
            @Expose
            val minPayment: Double = 0.0,

            @SerializedName("max_payment")
            @Expose
            val maxPayment: Double = 0.0,

            @SerializedName("min_payment_text")
            @Expose
            val minPaymentText: String = "",

            @SerializedName("max_payment_text")
            @Expose
            val maxPaymentText: String = "",

            @SerializedName("min_payment_error_text")
            @Expose
            val minPaymentErrorText: String = "",

            @SerializedName("max_payment_error_text")
            @Expose
            val maxPaymentErrorText: String = ""
    )

    data class AdditionalInfo(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("detail")
            @Expose
            val detail: List<Attribute> = listOf()
    )
    data class Attribute(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )
}