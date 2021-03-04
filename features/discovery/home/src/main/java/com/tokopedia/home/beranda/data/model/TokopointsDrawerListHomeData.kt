package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class TokopointsDrawerListHomeData (
        @SerializedName("tokopointsDrawerList")
        val tokopointsDrawerList: TokopointsDrawerList = TokopointsDrawerList()
)