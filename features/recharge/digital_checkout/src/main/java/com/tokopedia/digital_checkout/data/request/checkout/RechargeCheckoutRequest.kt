package com.tokopedia.digital_checkout.data.request.checkout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RechargeCheckoutRequest(
    @SerializedName("voucher_code")
    @Expose
    val voucherCode: String = "",
    @SerializedName("transaction_amount")
    @Expose
    val transactionAmount: Long = 0,
    @SerializedName("language")
    @Expose
    val language: String = "",
    @SerializedName("ip_address")
    @Expose
    val ipAddress: String = "",
    @SerializedName("user_agent")
    @Expose
    val userAgent: String = "",
    @SerializedName("device_id")
    @Expose
    val deviceId: Long = 0,
    @SerializedName("is_hidden_cart")
    @Expose
    val isHiddenCart: Boolean = false,
    @SerializedName("pid")
    @Expose
    val pid: Long = -1,
    @SerializedName("back_url")
    @Expose
    val backUrl: String = "",
    @SerializedName("user_id")
    @Expose
    val userId: String = "",
    @SerializedName("cart_type")
    @Expose
    val cartType: String = "",
    @SerializedName("cart_id")
    @Expose
    val cartId: String = "",
    @SerializedName("create_subscription")
    @Expose
    val createSubscription: Boolean = false,
    @SerializedName("fintech_products")
    @Expose
    val fintechProducts: List<RechargeCheckoutFintechProduct> = emptyList(),
    @SerializedName("instant")
    @Expose
    val instant: Boolean = false,
    @SerializedName("cart_fields")
    @Expose
    val cartFields: List<RechargeCheckoutCartField> = emptyList(),
    @SerializedName("deals_ids")
    @Expose
    val dealsIds: List<Long> = emptyList(),
    @SerializedName("advertisement_id")
    @Expose
    val advertisementId: String = "",
    @SerializedName("payment_gateway_code")
    @Expose
    val paymentGatewayCode: String = "",
    @SerializedName("is_use_points_only")
    @Expose
    val isUsePointsOnly: String = "",
    @SerializedName("app_version")
    @Expose
    val appVersion: String = "",
    @SerializedName("fingerprint_data")
    @Expose
    val fingerprintData: String = "",
    @SerializedName("fingerprint_hash")
    @Expose
    val fingerprintHash: String = "",
    @SerializedName("third_party_data")
    @Expose
    val thirdPartyData: ThirdPartyData = ThirdPartyData()
)

class RechargeCheckoutFintechProduct(
    @SerializedName("transaction_type")
    @Expose
    val transactionType: String = "",
    @SerializedName("checkout_metadata")
    @Expose
    val checkoutMetadata: String = ""
)

class RechargeCheckoutCartField(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("value")
    @Expose
    val value: String = "",
)

class ThirdPartyData(
    @SerializedName("redirect_url_on_success")
    @Expose
    val redirectUrlOnSuccess: String = "",
    @SerializedName("redirect_url_on_failure")
    @Expose
    val redirectUrlOnFailure: String = "",
    @SerializedName("custom_profile_code")
    @Expose
    val customProfileCode: String = "",
    @SerializedName("thank_you_order_url")
    @Expose
    val thankYouOrderUrl: String = "",
    @SerializedName("thank_you_order_url_apps")
    @Expose
    val thankYouOrderUrlApps: String = "",
    @SerializedName("thank_you_home_url")
    @Expose
    val thankYouHomeUrl: String = "",
    @SerializedName("thank_you_home_url_apps")
    @Expose
    val thankYouHomeUrlApps: String = ""
)