package com.tokopedia.sellerhomedrawer.data.userdata.wallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Action (

    @SerializedName("__typename")
    @Expose
    var typename: String? = "",
    @SerializedName("text")
    @Expose
    var text: String? = "",
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = "",
    @SerializedName("applinks")
    @Expose
    var applinks: String? = "",
    @SerializedName("visibility")
    @Expose
    var visibility: String? = ""

)
