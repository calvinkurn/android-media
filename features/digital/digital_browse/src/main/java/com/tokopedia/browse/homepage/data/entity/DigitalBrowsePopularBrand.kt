package com.tokopedia.browse.homepage.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowsePopularBrand(
        @SerializedName("id")
        @Expose
        var id: Int,
        @SerializedName("name")
        @Expose
        var name: String?,
        @SerializedName("isNew")
        @Expose
        var isNew: Boolean,
        @SerializedName("logoUrl")
        @Expose
        var logoUrl: String?,
        @SerializedName("url")
        @Expose
        var url: String?
)
