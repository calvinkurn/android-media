package com.tokopedia.flashsale.management.product.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSaleMutationResponseGQL(
        @SerializedName("mojito_submit_product")
        @Expose val flashSaleSubmitProduct: FlashSaleSubmitProduct)

class FlashSaleSubmitProduct(
        @SerializedName("data")
        @Expose val flashSaleSubmitProductData: List<FlashSaleSubmitProductData>,

        @SerializedName("message")
        @Expose val message: String) {
    companion object {
        val SUCCESS = "success"
    }

    fun isSuccess() = SUCCESS.equals(message, true)
}

data class FlashSaleSubmitProductData(
        @SerializedName("criteria_id")
        @Expose val criteriaId: Int,
        @SerializedName("is_available")
        @Expose val isAvailable: Boolean,
        @SerializedName("message")
        @Expose val message: String)
