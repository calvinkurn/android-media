package com.tokopedia.sellerhomedrawer.data.userdata.wallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Action {

    @SerializedName("__typename")
    @Expose
    var typename: String? = null
    @SerializedName("text")
    @Expose
    var text: String? = null
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null
    @SerializedName("applinks")
    @Expose
    var applinks: String? = null
    @SerializedName("visibility")
    @Expose
    var visibility: String? = null

}
