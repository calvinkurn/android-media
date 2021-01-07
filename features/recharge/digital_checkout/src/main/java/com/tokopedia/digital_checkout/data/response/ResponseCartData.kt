package com.tokopedia.digital_checkout.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.digital_checkout.data.response.AttributesCart
import com.tokopedia.digital_checkout.data.response.RelationshipsCart

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
