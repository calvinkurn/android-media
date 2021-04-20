package com.tokopedia.atc_common.data.model.response.ocs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OvoValidationResponse(
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("applink")
        @Expose
        val applink: String = "",
        @SerializedName("ovo_insufficient_balance")
        @Expose
        val ovoInsufficientBalance: OvoInsufficientBalance = OvoInsufficientBalance()
)

class OvoInsufficientBalance(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("details")
        @Expose
        val details: OvoInsufficientDetails = OvoInsufficientDetails(),
        @SerializedName("buttons")
        @Expose
        val buttons: OvoInsufficientButton = OvoInsufficientButton()
)

class OvoInsufficientDetails(
        @SerializedName("product_price")
        @Expose
        val productPrice: Long = 0,
        @SerializedName("shipping_estimation")
        @Expose
        val shippingEstimation: Int = 0,
        @SerializedName("ovo_balance")
        @Expose
        val ovoBalance: Int = 0,
        @SerializedName("topup_balance")
        @Expose
        val topupBalance: Int = 0
)

class OvoInsufficientButton(
        @SerializedName("ovo_topup_button")
        @Expose
        val topupButton: OvoInsufficientTopup = OvoInsufficientTopup(),
        @SerializedName("other_methods_button")
        @Expose
        val otherMethodButton: OvoInsufficientTopup = OvoInsufficientTopup()
)

data class OvoInsufficientTopup(
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("applink")
        @Expose
        val applink: String = "",
        @SerializedName("enable")
        @Expose
        val enable: Boolean = false
)