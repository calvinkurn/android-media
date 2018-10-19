package com.tokopedia.promocheckout.detail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Data {

    @SerializedName("hachikoCouponDetail")
    @Expose
    var hachikoCouponDetail: HachikoCouponDetail? = null

}
