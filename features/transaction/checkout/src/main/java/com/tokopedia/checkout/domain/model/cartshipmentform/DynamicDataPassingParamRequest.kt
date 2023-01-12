package com.tokopedia.checkout.domain.model.cartshipmentform

import com.google.gson.annotations.SerializedName

data class DynamicDataPassingParamRequest(
    @SerializedName("data")
    var data: List<DynamicDataParam> = emptyList()
) {

    data class DynamicDataParam(
        @SerializedName("level")
        var level: String = "",

        @SerializedName("parent_unique_id")
        var parentUniqueId: String = "",

        @SerializedName("unique_id")
        var uniqueId: String = "",

        @SerializedName("attribute")
        var attribute: String = "",

        @SerializedName("value")
        var value: String = ""
    )
}
