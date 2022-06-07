package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata.CheckoutErrorMetadata

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
    @SerializedName("error_state")
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
) {

    fun isSuccess(): Boolean = success == TokoFoodCartUtil.SUCCESS_STATUS_INT

    fun getErrorMetadataObject(): CheckoutErrorMetadata? {
        return try {
            Gson().fromJson(errorMetadata, CheckoutErrorMetadata::class.java)
        } catch (ex: Exception) {
            null
        }
    }

}

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