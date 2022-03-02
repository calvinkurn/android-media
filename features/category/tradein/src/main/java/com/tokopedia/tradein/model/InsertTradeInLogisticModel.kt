package com.tokopedia.tradein.model


import com.google.gson.annotations.SerializedName

data class InsertTradeInLogisticModel(
    @SerializedName("insertTradeInLogisticPreference")
    var insertTradeInLogisticPreference: InsertTradeInLogisticPreference
) {
    data class InsertTradeInLogisticPreference(
        @SerializedName("ErrCode")
        var errCode: Int,
        @SerializedName("ErrMessage")
        var errMessage: String,
        @SerializedName("IsSuccess")
        var isSuccess: Boolean
    )
}