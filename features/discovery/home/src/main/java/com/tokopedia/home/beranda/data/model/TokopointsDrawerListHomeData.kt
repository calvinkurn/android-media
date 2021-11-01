package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsDrawerListHomeData (
        @Expose
        @SerializedName("tokopointsDrawerList")
        val tokopointsDrawerList: TokopointsDrawerList = TokopointsDrawerList()
)