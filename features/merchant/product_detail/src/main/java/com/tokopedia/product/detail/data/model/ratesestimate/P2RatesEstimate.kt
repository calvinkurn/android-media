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

        @SerializedName("bottomsheet")
        @Expose
        val errorBottomSheet: ErrorBottomSheet = ErrorBottomSheet()
)

data class P2RatesEstimateData(
        @SerializedName("totalService")
        @Expose
        val totalService: Int = 0,

        @SerializedName("courierLabel")
        @Expose
        val instanLabel: String = "",

        @SerializedName("cheapestShippingPrice")
        @Expose
        val cheapestShippingPrice: Double = 0.0,

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

        @SerializedName("errors")
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
)

data class ErrorBottomSheet(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("iconURL")
        @Expose
        val iconURL: String = "",

        @SerializedName("subtitle")
        @Expose
        val subtitle: String = "",

        @SerializedName("buttonCopy")
        @Expose
        val buttonCopy: String = ""
)