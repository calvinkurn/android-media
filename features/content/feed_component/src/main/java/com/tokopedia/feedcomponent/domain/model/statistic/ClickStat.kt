package com.tokopedia.feedcomponent.domain.model.statistic

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class ClickStat(

        @SerializedName("fmt")
        val fmt: String = "0",

        @SerializedName("value")
        val value: Int = 0
)