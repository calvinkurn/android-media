package com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Owner (

    @SerializedName("is_gold_merchant")
    @Expose
    var isGoldMerchant: Boolean? = false,
    @SerializedName("is_seller")
    @Expose
    var isSeller: Boolean? = false

)
