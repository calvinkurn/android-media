package com.tokopedia.sellerhomedrawer.data.userdata.notifications

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResolutionAs (

    @SerializedName("buyer")
    @Expose
    var resolutionAsBuyer: Int = 0,
    @SerializedName("seller")
    @Expose
    var resolutionAsSeller: Int = 0

)