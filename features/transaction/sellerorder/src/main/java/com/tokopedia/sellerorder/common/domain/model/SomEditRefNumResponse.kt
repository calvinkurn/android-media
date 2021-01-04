package com.tokopedia.sellerorder.common.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-26.
 */
data class SomEditRefNumResponse (
        @SerializedName("data")
        @Expose
        val data: Data = Data(),

        @SerializedName("errors")
        @Expose
        var listError: List<Error> = listOf()
) {

    data class Data (
            @SerializedName("mpLogisticEditRefNum")
            @Expose
            var mpLogisticEditRefNum: MpLogisticEditRefNum = MpLogisticEditRefNum()) {

        data class MpLogisticEditRefNum(
                @SerializedName("message")
                @Expose
                var listMessage: List<String> = listOf()
        )
    }

    data class Error(
            @SerializedName("message")
            @Expose
            var message: String = "")
}
