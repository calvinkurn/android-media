package com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Stats {

    @SerializedName("__typename")
    @Expose
    var typename: String? = null
    @SerializedName("shop_total_transaction")
    @Expose
    var shopTotalTransaction: String? = null
    @SerializedName("shop_item_sold")
    @Expose
    var shopItemSold: String? = null

}
