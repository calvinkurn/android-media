package com.tokopedia.flight.search.data.api.single.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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