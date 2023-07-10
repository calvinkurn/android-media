package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class MoveAction(

    @SerializedName("type")
    var type: String? = "navigation",

    @SerializedName("value")
    var value: String? = "activeTab=2&componentId=2&rpc_sortfilter_location=1&rpc_sortfilter_category=123"

)
