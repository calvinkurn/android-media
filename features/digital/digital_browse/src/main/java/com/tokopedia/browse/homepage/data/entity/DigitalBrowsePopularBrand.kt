package com.tokopedia.browse.homepage.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowsePopularBrand(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("isNew")
        @Expose
        val isNew: Boolean,
        @SerializedName("logoUrl")
        @Expose
        val logoUrl: String,
        @SerializedName("url")
        @Expose
        val url: String
)
