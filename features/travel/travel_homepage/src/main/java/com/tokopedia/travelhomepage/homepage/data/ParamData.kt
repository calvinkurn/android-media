package com.tokopedia.travelhomepage.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2020-03-09
 */

data class ParamData(
        @SerializedName("product")
        @Expose
        val product: String = "ALL",

        @SerializedName("countryID")
        @Expose
        val countryID: String = "ID",

        @SerializedName("platform")
        @Expose
        val platform: String = "SUBHOMEPAGE"
)