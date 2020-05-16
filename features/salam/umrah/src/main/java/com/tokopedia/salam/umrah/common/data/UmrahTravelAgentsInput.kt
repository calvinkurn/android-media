package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by firman on 20/01/19
 */

data class UmrahTravelAgentsInput(
        @SerializedName("page")
        @Expose
        var page : Int = 1,
        @SerializedName("limit")
        @Expose
        var limit : Int = 20,
        @SerializedName("flags")
        @Expose
        var flags : List<String> = emptyList()
)