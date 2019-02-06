package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataPromoCheckoutList {

    @SerializedName("tokopointsCouponList")
    @Expose
    var tokopointsCouponList: TokopointsCouponList? = null

}
