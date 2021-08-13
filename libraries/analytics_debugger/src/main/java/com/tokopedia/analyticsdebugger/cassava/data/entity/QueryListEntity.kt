package com.tokopedia.analyticsdebugger.cassava.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 07/04/2021
 */
class QueryListEntity(
        @SerializedName("journey_id")
        @Expose
        val journeyId: Int = 0,
        @SerializedName("journey_name")
        @Expose
        val journeyName: String = "",
        @SerializedName("platform")
        @Expose
        val platform: String = "",
        @SerializedName("data")
        @Expose
        val regexData: List<QueryDataLayer> = listOf()
) {
    class DataResponse(
            @SerializedName("data")
            @Expose
            val data: QueryListEntity = QueryListEntity()
    )
}

class QueryDataLayer(
        @SerializedName("datalayer_id")
        @Expose
        val dataLayerId: Int = 0,
        @SerializedName("datalayer")
        @Expose
        val dataLayer: Map<String, Any> = mapOf()
)