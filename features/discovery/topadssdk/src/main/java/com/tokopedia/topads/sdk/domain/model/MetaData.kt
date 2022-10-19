package com.tokopedia.topads.sdk.domain.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class MetaData(
    @SerializedName("display")
    var display: String? = null
) {

    constructor(jsonObject: JSONObject) : this() {
        if (!jsonObject.isNull("display")) {
            display = jsonObject.getString("display")
        }
    }
}
