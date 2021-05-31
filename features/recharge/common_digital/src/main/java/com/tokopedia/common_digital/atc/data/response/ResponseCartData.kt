package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseCartData(
        @SerializedName("type")
        @Expose
        var type: String? = null,

        @SerializedName("id")
        @Expose
        var id: String? = null,

        @SerializedName("attributes")
        @Expose
        var attributes: AttributesCart? = null,

        @SerializedName("relationships")
        @Expose
        var relationships: RelationshipsCart? = null
)
