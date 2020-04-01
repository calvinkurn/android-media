package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage.Info
import com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage.Owner
import com.tokopedia.sellerhomedrawer.data.userdata.shopinfomoengage.Stats

data class ShopInfoMoengage (
        @SerializedName("info")
        @Expose
        var info: Info? = Info(),
        @SerializedName("owner")
        @Expose
        var owner: Owner? = Owner(),
        @SerializedName("stats")
        @Expose
        var stats: Stats? = Stats()

)