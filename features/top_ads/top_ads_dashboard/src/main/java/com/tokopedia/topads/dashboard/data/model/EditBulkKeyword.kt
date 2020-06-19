package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 5/6/20.
 */

class EditBulkKeyword(
        @field:SerializedName("keywordID")
        var keywordID: String,
        @field:SerializedName("groupID")
        var groupID: String?,
        @field:SerializedName("keywordPriceBid")
        var keywordPriceBid: Float?
)

