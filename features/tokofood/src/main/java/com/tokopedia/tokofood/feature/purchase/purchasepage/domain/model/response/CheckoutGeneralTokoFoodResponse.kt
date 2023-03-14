package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.response

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata.CheckoutErrorMetadata

data class CheckoutGeneralTokoFoodResponse(
    @SerializedName("checkout_cart_general")
    val checkoutGeneralTokoFood: CheckoutGeneralTokoFood = CheckoutGeneralTokoFood()
)

data class CheckoutGeneralTokoFood(
    @SerializedName("header")
    val header: Header = Header(),
    @SerializedName("data")
    val data: CheckoutGeneralTokoFoodData = CheckoutGeneralTokoFoodData()
)

data class CheckoutGeneralTokoFoodData(
    @SerializedName("success")
    val success: Int = 0,
    @SerializedName("error")
    val error: String = "",
    @SerializedName("error_metadata")
    val errorMetadata: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
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

    fun getErrorMessage(): String {
        return getErrorMetadataObject()?.popupErrorMessage?.text?.takeIf { it.isNotBlank() }
            ?: getErrorMetadataObject()?.popupMessage?.text?.takeIf { it.isNotBlank() }
            ?: error.takeIf { it.isNotBlank() }
            ?: message
    }

}

data class CheckoutGeneralTokoFoodMainData(
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @SerializedName("callback_url")
    val callbackUrl: String = "",
    @SerializedName("query_string")
    val queryString: String = ""
)
