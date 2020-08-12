package com.tokopedia.hotel.search.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 12/08/20
 */

data class ParamFilterV2(
        @SerializedName("name")
        @Expose
        var name: String = "",

        @SerializedName("values")
        @Expose
        var values: List<String> = listOf()
)