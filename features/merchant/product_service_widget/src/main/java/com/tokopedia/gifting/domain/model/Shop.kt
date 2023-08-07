package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

class Shop {
    @SerializedName("Name")
    var name: String = ""

    @SerializedName("ShopTier")
    var shopTier: Long = Int.ZERO.toLong()

    @SerializedName("ShopGrade" )
    var shopGrade: Int = 0
}
