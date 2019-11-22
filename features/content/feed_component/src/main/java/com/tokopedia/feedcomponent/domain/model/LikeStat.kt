package com.tokopedia.feedcomponent.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class LikeStat(

        @SerializedName("fmt")
        val fmt: String = "0",

        @SerializedName("value")
        val value: Int = 0,

        @SerializedName("checked")
        val checked: Boolean = false
)