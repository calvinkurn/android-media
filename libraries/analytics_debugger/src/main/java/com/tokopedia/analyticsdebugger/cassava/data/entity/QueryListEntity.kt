package com.tokopedia.analyticsdebugger.cassava.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 07/04/2021
 */
class QueryListEntity(
        @SuppressLint("Invalid Data Type")
        @SerializedName("journey_id")
        val journeyId: Int = 0,
        @SerializedName("journey_name")
        val journeyName: String = "",
        @SerializedName("platform")
        val platform: String = "",
        @SerializedName("data")
        val regexData: List<QueryDataLayer> = listOf()
) {
    class DataResponse(
            @SerializedName("data")
            val data: QueryListEntity = QueryListEntity()
    )
}

class QueryDataLayer(
        @SuppressLint("Invalid Data Type")
        @SerializedName("datalayer_id")
        val dataLayerId: Int = 0,
        @SerializedName("datalayer")
        val dataLayer: Map<String, Any> = mapOf()
)