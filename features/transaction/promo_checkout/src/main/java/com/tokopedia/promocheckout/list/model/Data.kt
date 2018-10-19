package com.tokopedia.promocheckout.list.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("tokopointsCouponList")
    @Expose
    var tokopointsCouponList: TokopointsCouponList? = null

}
