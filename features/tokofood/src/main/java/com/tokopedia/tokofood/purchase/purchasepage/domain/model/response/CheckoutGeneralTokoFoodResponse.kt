package com.tokopedia.tokofood.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class CheckoutGeneralTokoFoodResponse(
    @SerializedName("checkout_general_v2")
    @Expose
    val checkoutGeneralTokoFood: CheckoutGeneralTokoFood = CheckoutGeneralTokoFood()
)

data class CheckoutGeneralTokoFood(
    @SerializedName("header")
    @Expose
    val header: Header = Header(),
    @SerializedName("data")
    @Expose
    val data: CheckoutGeneralTokoFoodData = CheckoutGeneralTokoFoodData()
)

data class CheckoutGeneralTokoFoodData(
    @SerializedName("success")
    @Expose
    val success: Int = 0,
    @SerializedName("error")
    @Expose
    val error: String = "",
    @SerializedName("serror_state")
    @Expose
    val errorState: Int = 0,
    @SerializedName("error_metadata")
    @Expose
    val errorMetadata: String = "",
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("data")
    @Expose
    val data: CheckoutGeneralTokoFoodMainData = CheckoutGeneralTokoFoodMainData()
)

data class CheckoutGeneralTokoFoodMainData(
    @SerializedName("redirect_url")
    @Expose
    val redirectUrl: String = "",
    @SerializedName("callback_url")
    @Expose
    val callbackUrl: String = "",
    @SerializedName("query_string")
    @Expose
    val queryString: String = ""
)