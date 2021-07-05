package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 14/4/20.
 */
class TopadsManageGroupAdsInput(

        @field:SerializedName("groupOperation")
        var groupInput: GroupEditInput = GroupEditInput(),
        @field:SerializedName("keywordOperation")
        var keywordOperation: List<KeywordEditInput?>? = listOf(),
        @field:SerializedName("groupID")
        var groupID: String = "",
        @field:SerializedName("shopID")
        var shopID: String = "",
        @field:SerializedName("source")
        var source: String = ""

)

