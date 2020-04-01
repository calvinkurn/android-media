package com.tokopedia.sellerhomedrawer.data.userdata.wallet

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AbTag (

    @SerializedName("__typename")
    @Expose
    var typename: String? = "",
    @SerializedName("tag")
    @Expose
    var tag: String? = ""

)