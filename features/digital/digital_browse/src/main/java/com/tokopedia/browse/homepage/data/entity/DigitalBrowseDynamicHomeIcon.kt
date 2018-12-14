package com.tokopedia.browse.homepage.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 04/09/18.
 */

class DigitalBrowseDynamicHomeIcon(
        @SerializedName("categoryGroup")
        @Expose
        val dynamicHomeCategoryGroupEntities: List<DigitalBrowseCategoryGroupEntity>)
