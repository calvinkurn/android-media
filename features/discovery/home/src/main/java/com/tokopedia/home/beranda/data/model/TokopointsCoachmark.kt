package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokopointsCoachmark(
        @Expose
        @SerializedName("type")
        val type: String = "",
        @Expose
        @SerializedName("coachMarkContent")
        val coachmarkContent: List<TokopointsCoachmarkContent> = mutableListOf()
)