package com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Stats (

    @SerializedName("shop_total_transaction")
    @Expose
    var shopTotalTransaction: String? = "",
    @SerializedName("shop_item_sold")
    @Expose
    var shopItemSold: String? = ""

)
