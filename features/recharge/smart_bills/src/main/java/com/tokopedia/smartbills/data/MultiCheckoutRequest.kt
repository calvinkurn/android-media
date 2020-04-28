package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.RechargeField

class MultiCheckoutRequest (
    @SerializedName("attributes")
    @Expose
    var attributes: MultiCheckoutRequestAttributes = MultiCheckoutRequestAttributes()
) {

    class MultiCheckoutRequestAttributes (
        @SerializedName("items")
        @Expose
        var items: List<MultiCheckoutRequestItem> = listOf()
    )

    class MultiCheckoutRequestItem (
        @SerializedName("index")
        @Expose
        var index: Int = 0,
        @SerializedName("product_id")
        @Expose
        var productID: Int = 0,
        @SerializedName("fields")
        @Expose
        var fields: List<RechargeField> = listOf(),
        @SerializedName("cart_uuid")
        @Expose
        var cartUUID: String = ""
    )

}