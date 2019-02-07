package com.tokopedia.product.detail.data.model.estimasiongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatesEstimationModel (
        @SerializedName("shop")
        @Expose
        val shop: ShopModel = ShopModel(),

        @SerializedName("address")
        @Expose
        val address: AddressModel = AddressModel(),

        @SerializedName("rates")
        @Expose
        val rates: RatesModel = RatesModel()){

    data class Data (
        @SerializedName("data")
        @Expose
        val ratesEstimation: ArrayList<RatesEstimationModel> = ArrayList()
    )

    data class Response (
            @SerializedName("rates_estimate")
            @Expose
            val data: Data = Data()
    )
}
