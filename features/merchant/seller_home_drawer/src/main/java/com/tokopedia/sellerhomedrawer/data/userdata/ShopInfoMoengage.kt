package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.core.drawer2.data.pojo.Info
import com.tokopedia.core.drawer2.data.pojo.Owner
import com.tokopedia.core.drawer2.data.pojo.Stats

class ShopInfoMoengage {

    @SerializedName("info")
    @Expose
    var info: Info? = null
    @SerializedName("owner")
    @Expose
    var owner: Owner? = null
    @SerializedName("stats")
    @Expose
    var stats: Stats? = null

}