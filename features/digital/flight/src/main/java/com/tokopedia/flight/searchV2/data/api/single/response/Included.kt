package com.tokopedia.flight.searchV2.data.api.single.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.search.data.cloud.model.response.AttributesInc

/**
 * Created by Rizky on 23/10/18.
 */
class Included<Attributes: AttributesInc>(
        @SerializedName("type")
        @Expose
        val type: String,
        @SerializedName("id")
        @Expose
        val id: String,
        @SerializedName("attributes")
        @Expose
        var attributes: Attributes
)