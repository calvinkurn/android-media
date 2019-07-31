package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModerateSubmitInput (

        @SerializedName("shopIDs")
        @Expose
        var shopIds:ArrayList<Int> = ArrayList(),

        @SerializedName("status")
        @Expose
        var status:Int = 0,

        @SerializedName("notes")
        @Expose
        var notes:String = "",

        @SerializedName("responseDesc")
        @Expose
        var responseDesc:String = ""
)
