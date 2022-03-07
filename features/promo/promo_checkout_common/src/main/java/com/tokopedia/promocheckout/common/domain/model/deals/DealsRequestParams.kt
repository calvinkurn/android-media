package com.tokopedia.promocheckout.common.domain.model.deals

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DealsRequestParams (
    @SerializedName("categoryName")
    @Expose
    val categoryName: String = "deals",
    @SerializedName("data")
    @Expose
    val data: DataRequest = DataRequest()
)

data class DataRequest(
        @SerializedName("codes")
        @Expose
        val code: List<String> = emptyList(),
        @SerializedName("book")
        @Expose
        val book: Boolean = false,
        @SerializedName("meta_data")
        @Expose
        val meta_data: String = "",
        @SerializedName("grand_total")
        @Expose
        val grand_total: Int = 0,
        @SerializedName("language")
        @Expose
        val language: String = "id",
        @SerializedName("state")
        @Expose
        val state: String = "checkout"
)