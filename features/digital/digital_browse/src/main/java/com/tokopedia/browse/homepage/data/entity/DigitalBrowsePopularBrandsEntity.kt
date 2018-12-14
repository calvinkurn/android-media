package com.tokopedia.browse.homepage.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowsePopularBrandsEntity(
        @SerializedName("category")
        @Expose
        val category: String,
        @SerializedName("data")
        @Expose
        val data: List<DigitalBrowsePopularBrand>
)
