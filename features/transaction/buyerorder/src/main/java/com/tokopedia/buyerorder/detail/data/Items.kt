package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Items(
    @SerializedName("categoryID")
    @Expose
    var categoryID: Int = 0,

    @SerializedName("title")
    @Expose
    var title: String = "",

    @SerializedName("category")
    @Expose
    var category: String = "",

    @SerializedName("tapActions")
    @Expose
    var tapActions: List<ActionButton> = emptyList(),

    @SerializedName("price")
    @Expose
    var price: String = "",

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String = "",

    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0,

    @SerializedName("actionButtons")
    @Expose
    var actionButtons: List<ActionButton> = emptyList(),

    @SerializedName("metaData")
    @Expose
    var metaData: String = "",

    @SerializedName("totalPrice")
    @Expose
    var totalPrice: String = "",

    @SerializedName("trackingNumber")
    @Expose
    var trackingNumber: String = "",

    @SerializedName("invoiceID")
    @Expose
    var invoiceId: String = "",

    var isTapActionsLoaded: Boolean = false,

    var isActionButtonLoaded: Boolean = false
) : Serializable