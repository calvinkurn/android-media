package com.tokopedia.common_digital.cart.data.entity.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 3/9/17.
 */

class Parameter {
    @SerializedName("merchant_code")
    @Expose
    var merchantCode: String? = null
    @SerializedName("profile_code")
    @Expose
    var profileCode: String? = null
    @SerializedName("transaction_id")
    @Expose
    var transactionId: String? = null
    @SerializedName("transaction_code")
    @Expose
    var transactionCode: String? = null
    @SerializedName("transaction_date")
    @Expose
    var transactionDate: String? = null
    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null
    @SerializedName("customer_email")
    @Expose
    var customerEmail: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("currency")
    @Expose
    var currency: String? = null
    @SerializedName("items[name]")
    @Expose
    var itemsName: List<String>? = null
    @SerializedName("items[quantity]")
    @Expose
    var itemsQuantity: List<Int>? = null
    @SerializedName("items[price]")
    @Expose
    var itemsPrice: List<Int>? = null
    @SerializedName("signature")
    @Expose
    var signature: String? = null
    @SerializedName("language")
    @Expose
    var language: String? = null
    @SerializedName("user_defined_varue")
    @Expose
    var userDefinedvarue: String? = null
    @SerializedName("nid")
    @Expose
    var nid: String? = null
    @SerializedName("state")
    @Expose
    var state: Int = 0
    @SerializedName("fee")
    @Expose
    var fee: String? = null
    @SerializedName("payments[amount]")
    @Expose
    var paymentsAmount: List<String>? = null
    @SerializedName("payments[name]")
    @Expose
    var paymentsName: List<String>? = null
}
