package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common.topupbills.data.RechargeField
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier

class MultiCheckoutRequest (
    @SerializedName("attributes")
    @Expose
    val attributes: MultiCheckoutRequestAttributes = MultiCheckoutRequestAttributes()
) {

    class MultiCheckoutRequestAttributes (
        @SerializedName("identifier")
        @Expose
        val identifier: RequestBodyIdentifier = RequestBodyIdentifier(),
        @SerializedName("items")
        @Expose
        val items: List<MultiCheckoutRequestItem> = listOf()
    )

    class MultiCheckoutRequestItem (
        @SerializedName("index")
        @Expose
        val index: Int = 0,
        @SerializedName("product_id")
        @Expose
        val productID: Int = 0,
        @SerializedName("fields")
        @Expose
        val fields: List<RechargeField> = listOf(),
        @SerializedName("cart_uuid")
        @Expose
        val cartUUID: String = ""
    )

}