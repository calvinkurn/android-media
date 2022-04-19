package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsDrawerList (
        @Expose
        @SerializedName("offFlag")
        val offFlag: String = "",

        @Expose
        @SerializedName("drawerList")
        val drawerList: List<TokopointsDrawer> = mutableListOf(),

        @Expose
        @SerializedName("coachMarkList")
        val coachmarkList: List<TokopointsCoachmark> = mutableListOf()
)