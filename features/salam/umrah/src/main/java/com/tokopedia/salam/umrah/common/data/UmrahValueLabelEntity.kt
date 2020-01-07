package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 11/10/2019
 */
class UmrahValueLabelEntity (
        @SerializedName("value")
        @Expose
        val value: String = "",
        @SerializedName("label")
        @Expose
        val label: String = ""
)