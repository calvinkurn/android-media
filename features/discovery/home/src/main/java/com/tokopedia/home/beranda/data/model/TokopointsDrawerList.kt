package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.SerializedName

data class TokopointsDrawerList (

        @SerializedName("offFlag")
        val offFlag: String = "",

        @SerializedName("drawerList")
        val drawerList: List<TokopointsDrawer> = mutableListOf()
)