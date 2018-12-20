package com.tokopedia.promocheckout.list.model.listcoupon

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TokopointsCouponList {

    @SerializedName("tokopointsCouponData")
    @Expose
    var tokopointsCouponData: List<PromoCheckoutListModel>? = null
    @SerializedName("tokopointsPaging")
    @Expose
    var tokopointsPaging: TokopointsPaging? = null
    @SerializedName("tokopointsExtraInfo")
    @Expose
    var tokopointsExtraInfo: List<Any>? = null
    @SerializedName("tokopointsEmptyMessage")
    @Expose
    var tokopointsEmptyMessage: TokopointsEmptyMessage? = null

}
