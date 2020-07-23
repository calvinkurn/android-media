package com.tokopedia.topads.dashboard.data.model.insightkey

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MutationData(
        @SerializedName("action")
        val action: String = "",
        @SerializedName("keyword")
        val keyword: Keyword = Keyword()
) : Serializable
