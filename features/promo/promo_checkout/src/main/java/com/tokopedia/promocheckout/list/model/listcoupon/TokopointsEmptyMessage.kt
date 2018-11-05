package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokopointsEmptyMessage {

    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("subTitle")
    @Expose
    var subTitle: String? = null

}
