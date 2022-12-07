package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PromoCouponEntity(
    @Expose
    @SerializedName("coupons")
    var coupons: MutableList<CouponValueEntity> = mutableListOf(),
    @Expose
    @SerializedName("tokopointsEmptyMessage")
    var emptyMessage: Map<String, String> = mapOf(),
    @SerializedName("tokopointsPaging")
    var paging: TokopointPaging =  TokopointPaging(),
)
