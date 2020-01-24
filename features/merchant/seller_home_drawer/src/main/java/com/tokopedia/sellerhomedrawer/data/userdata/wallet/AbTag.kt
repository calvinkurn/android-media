package com.tokopedia.sellerhomedrawer.data.userdata.wallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AbTag {

    @SerializedName("__typename")
    @Expose
    var typename: String? = null
    @SerializedName("tag")
    @Expose
    var tag: String? = null

}