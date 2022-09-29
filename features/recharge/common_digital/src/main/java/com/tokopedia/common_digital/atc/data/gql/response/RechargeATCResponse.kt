package com.tokopedia.common_digital.atc.data.gql.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_digital.atc.data.response.AtcErrorPage

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
data class RechargeATCResponse(
    @SerializedName("data")
    @Expose
    val data: RechargeATCData = RechargeATCData(),
    @SerializedName("errors")
    @Expose
    val errors: List<RechargeATCError> = emptyList()
) {
    data class Response(
        @SerializedName("rechargeAddToCartV2")
        @Expose
        val atcResponse: RechargeATCResponse = RechargeATCResponse()
    )
}

class RechargeATCData(
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("attributes")
    @Expose
    val attributes: RechargeATCAttribute = RechargeATCAttribute()
)

class RechargeATCAttribute(
    @SerializedName("user_id")
    @Expose
    val userId: Long = 0,
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
    @SerializedName("price")
    @Expose
    val price: String = "",
    @SerializedName("price_plain")
    @Expose
    val pricePlain: Double = 0.0,
    @SerializedName("instant_checkout")
    @Expose
    val isInstantCheckout: Boolean = false,
    @SerializedName("need_otp")
    @Expose
    val isNeedOtp: Boolean = false,
    @SerializedName("sms_state")
    @Expose
    val smsState: String = "",
    @SerializedName("voucher_autocode")
    @Expose
    val voucherAutoCode: String = "",
    @SerializedName("user_input_price")
    @Expose
    val userInputPrice: Double = 0.0,
    @SerializedName("user_open_payment")
    @Expose
    val userOpenPayment: RechargeATCOpenPayment = RechargeATCOpenPayment(),
    @SerializedName("enable_voucher")
    @Expose
    val isEnableVoucher: Boolean = false,
    @SerializedName("is_coupon_active")
    @Expose
    val isCouponActive: Int = 0,
    @SerializedName("default_promo_dialog_tab")
    @Expose
    val defaultPromoDialogTab: String = "",
    @SerializedName("channel_id")
    @Expose
    val channelId: Long = 0,
    @SerializedName("main_info")
    @Expose
    val mainInfo: List<RechargeATCMainInfo> = emptyList()
)

class RechargeATCOpenPayment(
    @SerializedName("min_payment_text")
    @Expose
    val minPaymentText: String = "",
    @SerializedName("max_payment_text")
    @Expose
    val maxPaymentText: String = "",
    @SerializedName("min_payment")
    @Expose
    val minPayment: Int = 0,
    @SerializedName("max_payment")
    @Expose
    val maxPayment: Int = 0,
    @SerializedName("min_payment_error_text")
    @Expose
    val minPaymentErrorText: String = "",
    @SerializedName("max_payment_error_text")
    @Expose
    val maxPaymentErrorText: String = ""
)

class RechargeATCMainInfo(
    @SerializedName("label")
    @Expose
    val label: String = "",
    @SerializedName("value")
    @Expose
    val value: String = ""
)

class RechargeATCError(
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("applink_url")
    @Expose
    val applinkUrl: String = "",
    @SerializedName("atc_error_page")
    @Expose
    val atcErrorPage: AtcErrorPage = AtcErrorPage()
)