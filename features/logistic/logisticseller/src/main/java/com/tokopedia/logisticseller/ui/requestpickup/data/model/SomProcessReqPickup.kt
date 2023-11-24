package com.tokopedia.logisticseller.ui.requestpickup.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 2019-11-14.
 */
data class SomProcessReqPickup(
    @SerializedName("data")
    @Expose
    val data: Data = Data(),

    @SerializedName("errors")
    @Expose
    val listError: List<Error> = listOf()
) {

    data class Data(
        @SerializedName("mpLogisticRequestPickup")
        @Expose
        val mpLogisticRequestPickup: MpLogisticRequestPickup = MpLogisticRequestPickup()
    ) {

        @Parcelize
        data class MpLogisticRequestPickup(
            @SerializedName("message")
            @Expose
            val listMessage: List<String> = listOf()
        ) : Parcelable
    }

    data class Error(
        @SerializedName("message")
        @Expose
        val msgError: String = ""
    )
}
