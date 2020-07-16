package com.tokopedia.topads.dashboard.data.model.insightkey


import com.google.gson.annotations.SerializedName

data class MutationData(
    @SerializedName("action")
    val action: String = "",
    @SerializedName("keyword")
    val keyword: Keyword = Keyword()
)