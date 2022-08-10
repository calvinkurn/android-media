package com.tokopedia.digital_checkout.data.request.checkout

import com.google.gson.annotations.SerializedName

class RechargeCheckoutRequest(
    @SerializedName("voucher_code")
    val voucherCode: String = "",
    @SerializedName("transaction_amount")
    val transactionAmount: Long = 0,
    @SerializedName("language")
    val language: String = "",
    @SerializedName("ip_address")
    val ipAddress: String = "",
    @SerializedName("user_agent")
    val userAgent: String = "",
    @SerializedName("device_id")
    val deviceId: Long = 0,
    @SerializedName("is_hidden_cart")
    val isHiddenCart: Boolean = false,
    @SerializedName("pid")
    val pid: Long = -1,
    @SerializedName("back_url")
    val backUrl: String = "",
    @SerializedName("user_id")
    val userId: String = "",
    @SerializedName("cart_type")
    val cartType: String = "",
    @SerializedName("cart_id")
    val cartId: String = "",
    @SerializedName("create_subscription")
    val createSubscription: Boolean = false,
    @SerializedName("fintech_products")
    val fintechProducts: List<RechargeCheckoutFintechProduct> = emptyList(),
    @SerializedName("instant")
    val instant: Boolean = false,
    @SerializedName("cart_fields")
    val cartFields: List<RechargeCheckoutCartField> = emptyList(),
    @SerializedName("deals_ids")
    val dealsIds: List<Long> = emptyList(),
    @SerializedName("advertisement_id")
    val advertisementId: String = "",
    @SerializedName("payment_gateway_code")
    val paymentGatewayCode: String = "",
    @SerializedName("is_use_points_only")
    val isUsePointsOnly: String = "",
    @SerializedName("app_version")
    val appVersion: String = "",
    @SerializedName("fingerprint_data")
    val fingerprintData: String = "",
    @SerializedName("fingerprint_hash")
    val fingerprintHash: String = "",
    @SerializedName("third_party_data")
    val thirdPartyData: ThirdPartyData = ThirdPartyData()
)

class RechargeCheckoutFintechProduct(
    @SerializedName("transaction_type")
    val transactionType: String = "",
    @SerializedName("checkout_metadata")
    val checkoutMetadata: String = ""
)

class RechargeCheckoutCartField(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("value")
    val value: String = "",
)

class ThirdPartyData(
    @SerializedName("redirect_url_on_success")
    val redirectUrlOnSuccess: String = "",
    @SerializedName("redirect_url_on_failure")
    val redirectUrlOnFailure: String = "",
    @SerializedName("custom_profile_code")
    val customProfileCode: String = "",
    @SerializedName("thank_you_order_url")
    val thankYouOrderUrl: String = "",
    @SerializedName("thank_you_order_url_apps")
    val thankYouOrderUrlApps: String = "",
    @SerializedName("thank_you_home_url")
    val thankYouHomeUrl: String = "",
    @SerializedName("thank_you_home_url_apps")
    val thankYouHomeUrlApps: String = ""
)