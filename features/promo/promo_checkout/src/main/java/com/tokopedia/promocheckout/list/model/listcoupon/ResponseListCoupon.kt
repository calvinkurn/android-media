package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseListCoupon {

    @SerializedName("data")
    @Expose
    var dataPromoCheckoutList: DataPromoCheckoutList? = null

}
