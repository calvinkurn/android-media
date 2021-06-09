package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoPayment {
    @SerializedName("payment_default_status")
    @Expose
    var paymentDefaultStatus: String? = null

    @SerializedName("payment_id")
    @Expose
    var paymentId: String? = null

    @SerializedName("payment_image")
    @Expose
    var paymentImage: String? = null

    @SerializedName("payment_info")
    @Expose
    var paymentInfo: Long = 0

    @SerializedName("payment_name")
    @Expose
    var paymentName: String? = null
}