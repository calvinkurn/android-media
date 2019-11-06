package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-05.
 */
data class SomReasonRejectData (
        @SerializedName("data")
        @Expose
        val data: Data = Data()) {

    data class Data (
            @SerializedName("som_reject_reason")
            @Expose
            val listSomRejectReason: List<SomRejectReason> = listOf()) {

        data class SomRejectReason (
                @SerializedName("reason_code")
                @Expose
                val reasonCode: Int = 0,

                @SerializedName("reason_text")
                @Expose
                val reasonText: String = "",

                @SerializedName("child")
                @Expose
                val listChild: List<Child> = listOf()) {

            data class Child(
                    @SerializedName("reason_code")
                    @Expose
                    val reasonCode: Int = 0,

                    @SerializedName("reason_text")
                    @Expose
                    val reasonText: String = "")}

        }
    }