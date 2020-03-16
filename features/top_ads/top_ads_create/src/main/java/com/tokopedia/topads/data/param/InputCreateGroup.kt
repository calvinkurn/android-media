package com.tokopedia.topads.data.param

import com.google.gson.annotations.SerializedName


open class InputCreateGroup(
        @SerializedName("keywords")
        var keywords: List<KeywordsItem> = listOf(),
        @SerializedName("shopID")
        var shopID: String = "",
        @SerializedName("group")
        var group: Group = Group()
)