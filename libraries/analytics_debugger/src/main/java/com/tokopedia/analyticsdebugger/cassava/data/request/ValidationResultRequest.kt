package com.tokopedia.analyticsdebugger.cassava.data.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 11/04/2021
 */
class ValidationResultRequest(
        @SerializedName("version")
        @Expose
        val version: String,
        @SerializedName("token")
        @Expose
        val token: String,
        @SerializedName("data")
        @Expose
        val data: List<ValidationResultData>
)

class ValidationResultData(
        @SerializedName("datalayer_id")
        @Expose
        val dataLayerId: Int,
        @SerializedName("result")
        @Expose
        val result: Boolean,
        @SerializedName("error_message")
        @Expose
        val errorMessage: String
)