package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataPromoCheckoutDetail {

    @SerializedName("hachikoCouponDetail")
    @Expose
    var promoCheckoutDetailModel: PromoCheckoutDetailModel? = null

}
