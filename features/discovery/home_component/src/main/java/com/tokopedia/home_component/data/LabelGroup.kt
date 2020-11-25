package com.tokopedia.home_component.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Devara on 02/04/20
 */
data class LabelGroup(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("position")
        @Expose
        val position: String = "",
        @SerializedName("type")
        @Expose
        val type: String = ""
) 