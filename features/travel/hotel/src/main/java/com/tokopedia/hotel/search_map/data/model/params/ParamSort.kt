package com.tokopedia.hotel.search_map.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParamSort(
        @SerializedName("popularity")
        @Expose
        var popularity: Boolean = false,

        @SerializedName("price")
        @Expose
        var price: Boolean = false,

        @SerializedName("ranking")
        @Expose
        var ranking: Boolean = false,

        @SerializedName("reviewScore")
        @Expose
        var reviewScore: Boolean = false,

        @SerializedName("star")
        @Expose
        var star: Boolean = false,

        @SerializedName("sortDir")
        @Expose
        var sortDir: String = "desc"
)