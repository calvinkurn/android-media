package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ActionButtonList(
    @SerializedName("orderDetailTapAction")
    @Expose
    var actionButtonList: List<ActionButton> = emptyList()
)