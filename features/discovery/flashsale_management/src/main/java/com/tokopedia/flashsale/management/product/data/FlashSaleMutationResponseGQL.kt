package com.tokopedia.flashsale.management.product.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 25/10/18.
 */
data class FlashSaleMutationSubmitResponseGQL(
        @SerializedName("campaign_submit_product")
        @Expose val flashSaleDataContainer: FlashSaleDataContainer)

data class FlashSaleMutationReserveResponseGQL(
        @SerializedName("campaign_reserve_product")
        @Expose val flashSaleDataContainer: FlashSaleDataContainer)

data class FlashSaleMutationDeReserveResponseGQL(
        @SerializedName("campaign_dereserve_product")
        @Expose val flashSaleDataContainer: FlashSaleDataContainer)

class FlashSaleDataContainer(
        @SerializedName("data")
        @Expose val flashSaleCriteriaResponseData: List<FlashSaleCriteriaResponseData>,

        @SerializedName("message")
        @Expose val message: String,

        @SerializedName("status_code")
        @Expose val statusCode: Int) {
    companion object {
        val SUCCESS = "success"
    }

    fun isSuccess() = SUCCESS.equals(message, true)
}

data class FlashSaleCriteriaResponseData(
        @SerializedName("criteria_id")
        @Expose val criteriaId: Int,
        @SerializedName("is_available")
        @Expose val isAvailable: Boolean,
        @SerializedName("message")
        @Expose val message: String)
