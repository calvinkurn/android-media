package com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Owner {

    @SerializedName("__typename")
    @Expose
    var typename: String? = null
    @SerializedName("is_gold_merchant")
    @Expose
    var isGoldMerchant: Boolean? = null
    @SerializedName("is_seller")
    @Expose
    var isSeller: Boolean? = null

}
