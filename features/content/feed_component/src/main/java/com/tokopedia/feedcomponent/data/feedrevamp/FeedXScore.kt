package com.tokopedia.feedcomponent.data.feedrevamp

import com.google.gson.annotations.SerializedName

data class FeedXScore(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("value")
    val value: String = ""
){
    val isContentScore : Boolean
      get() = label == TOTAL_SCORE

    companion object{
        const val TOTAL_SCORE = "TotalScore"
    }
}
