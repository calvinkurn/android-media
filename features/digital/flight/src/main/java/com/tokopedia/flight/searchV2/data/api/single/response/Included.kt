package com.tokopedia.flight.searchV2.data.api.single.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Rizky on 23/10/18.
 */
class Included(
        @SerializedName("type")
        @Expose
        val type: String,
        @SerializedName("id")
        @Expose
        val id: String,
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes
)