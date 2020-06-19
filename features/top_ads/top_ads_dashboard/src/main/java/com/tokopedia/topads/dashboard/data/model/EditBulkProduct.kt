package com.tokopedia.topads.dashboard.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 5/6/20.
 */
class EditBulkProduct(

        @field:SerializedName("adID")
        var adID: String,
        @field:SerializedName("groupID")
        var groupID: String?,
        @field:SerializedName("priceBid")
        var priceBid: Float?

)