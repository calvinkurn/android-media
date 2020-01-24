package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TopadsDeposit {

    @SerializedName("topads_amount")
    @Expose
    val topadsAmount: Int? = null
    @SerializedName("is_topads_user")
    @Expose
    val isTopadsUser: Boolean? = null

}