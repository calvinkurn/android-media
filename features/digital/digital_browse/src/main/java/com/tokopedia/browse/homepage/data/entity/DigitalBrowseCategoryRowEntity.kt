package com.tokopedia.browse.homepage.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 03/09/18.
 */

class DigitalBrowseCategoryRowEntity(
        @SerializedName("id")
        @Expose
        val id: Int,
        @SerializedName("name")
        @Expose
        val name: String,
        @SerializedName("url")
        @Expose
        val url: String,
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String,
        @SerializedName("type")
        @Expose
        val type: String,
        @SerializedName("categoryId")
        @Expose
        val categoryId: Int,
        @SerializedName("applinks")
        @Expose
        val appLinks: String,
        @SerializedName("categoryLabel")
        @Expose
        val categoryLabel: String)
