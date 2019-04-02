package com.tokopedia.browse.homepage.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowseCategoryGroupEntity(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("desc")
        @Expose
        val desc: String = "",
        @SerializedName("categoryRows")
        @Expose
        val categoryRow: List<DigitalBrowseCategoryRowEntity> = arrayListOf())
