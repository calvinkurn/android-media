package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseDetailCoupon {

    @SerializedName("data")
    @Expose
    var dataPromoCheckoutDetail: DataPromoCheckoutDetail? = null

}
