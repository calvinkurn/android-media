package com.tokopedia.product.detail.data.model.ratesestimate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 10/02/21
 */

data class P2RatesEstimate(
        @SerializedName("warehouseID")
        @Expose
        val warehouseId: String = "",

        @SerializedName("products")
        @Expose
        val listfProductId: List<String> = listOf(),

        @SerializedName("data")
        @Expose
        val p2RatesData: P2RatesEstimateData = P2RatesEstimateData(),
)

data class P2RatesEstimateData(
        @SerializedName("totalService")
        @Expose
        val totalService: Int = 0,

        @SerializedName("isSupportInstantCourier")
        @Expose
        val isSupportInstantCourier: Boolean = false,

        @SerializedName("destination")
        @Expose
        val destination: String = "",

        @SerializedName("icon")
        @Expose
        val icon: String = "",

        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",

        @SerializedName("eTAText")
        @Expose
        val etaText: String = "",

        @SerializedName("eTAText")
        @Expose
        val p2RatesError: List<P2RatesError> = listOf()
)

data class P2RatesError(
        @SerializedName("Code")
        @Expose
        val errorCode: Int = 0,

        @SerializedName("Message")
        @Expose
        val errorMessage: String = "",

        @SerializedName("DevMessage")
        @Expose
        val devMessage: String = ""
) {
    companion object {
        const val ERROR_CODE_PRODUCT_ID_NOT_FOUND = 123213
    }
}