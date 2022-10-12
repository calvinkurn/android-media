package com.tokopedia.common_digital.atc.data.gql.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author Created By : Muhammad Furqan on Aug 5, 2022
 */
class RechargeATCRequest(
    @SerializedName("device_id")
    @Expose
    val deviceId: Long = 0,
    @SerializedName("fields")
    @Expose
    val fields: List<RechargeATCField> = emptyList(),
    @SerializedName("identifier")
    @Expose
    val identifier: RechargeATCIdentifier = RechargeATCIdentifier(),
    @SerializedName("instant_checkout")
    @Expose
    val instantCheckout: Boolean = false,
    @SerializedName("ip_address")
    @Expose
    val ipAddress: String = "",
    @SerializedName("product_id")
    @Expose
    val productId: Long = 0,
    @SerializedName("user_agent")
    @Expose
    val userAgent: String = "",
    @SerializedName("user_id")
    @Expose
    val userId: Long = 0,
    @SerializedName("order_id")
    @Expose
    val orderId: Long = 0,
    @SerializedName("profile_code")
    @Expose
    val profileCode: String = "",
    @SerializedName("enquiry_log_id")
    @Expose
    val enquiryLogId: Long = 0,
    @SerializedName("atc_source")
    @Expose
    val atcSource: String = ""
)

class RechargeATCField(
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("value")
    @Expose
    val value: String = ""
)

class RechargeATCIdentifier(
    @SerializedName("device_token")
    @Expose
    val deviceToken: String = "",
    @SerializedName("os_type")
    @Expose
    val osType: String = "",
    @SerializedName("user_id")
    @Expose
    val userId: String = ""
)