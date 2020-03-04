package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopadsDeposit (

    @SerializedName("topads_amount")
    @Expose
    val topadsAmount: Int? = 0,
    @SerializedName("is_topads_user")
    @Expose
    val isTopadsUser: Boolean? = false

)