package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 11/10/2019
 */
class UmrahItemEntity(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",
        @SerializedName("productUrl")
        @Expose
        val productUrl: String = "",
        @SerializedName("metaData")
        @Expose
        val metadata: String = ""
)