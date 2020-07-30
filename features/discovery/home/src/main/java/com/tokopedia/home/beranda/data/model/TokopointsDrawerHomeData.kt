package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class TokopointsDrawerHomeData (
    @SerializedName("tokopointsDrawer")
    val tokopointsDrawer: TokopointsDrawer = TokopointsDrawer()
)