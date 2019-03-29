package com.tokopedia.expresscheckout.data.entity.response.checkout

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

data class Reflect(
        @SerializedName("merchant_code")
        val merchantCode: String,

        @SerializedName("profile_code")
        val profileCode: String,

        @SerializedName("transaction_id")
        val transactionId: String,

        @SerializedName("transaction_code")
        val transactionCode: String,

        @SerializedName("currency")
        val currency: String,

        @SerializedName("amount")
        val amount: Int,

        @SerializedName("gateway_code")
        val gatewayCode: String,

        @SerializedName("gateway_type")
        val gatewayType: String,

        @SerializedName("fee")
        val fee: Int,

        @SerializedName("additional_fee")
        val additionalFee: Int,

        @SerializedName("user_defined_value")
        val userDefinedValue: String,

        @SerializedName("customer_email")
        val customerEmail: String,

        @SerializedName("state")
        val state: Int,

        @SerializedName("expired_on")
        val expiredOn: String,

        @SerializedName("updated_on")
        val updatedOn: String,

        @SerializedName("payment_details")
        val paymentDetails: ArrayList<PaymentDetail>,

        @SerializedName("items")
        val items: ArrayList<Item>,

        @SerializedName("valid_param")
        val validParam: String,

        @SerializedName("signature")
        val signature: String,

        @SerializedName("tokocash_usage")
        val tokocashUsage: Int,

        @SerializedName("pair_data")
        val pairData: String,

        @SerializedName("error_code")
        val errorCode: String,

        @SerializedName("use_3dsecure")
        val use3dsecure: Boolean
)