package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokopointsPaging {

    @SerializedName("hasNext")
    @Expose
    var isHasNext: Boolean = false

}
