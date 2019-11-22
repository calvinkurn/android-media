package com.tokopedia.feedcomponent.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class ViewStat(

        @SerializedName("fmt")
        val fmt: String = "0",

        @SerializedName("value")
        val value: Int = 0
)