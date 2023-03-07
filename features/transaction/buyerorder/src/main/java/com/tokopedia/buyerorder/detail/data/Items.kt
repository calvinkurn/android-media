package com.tokopedia.buyerorder.detail.data

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Items(
    @SerializedName("categoryID")
    @Expose
    val categoryID: Int = 0,

    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("category")
    @Expose
    val category: String = "",

    @SerializedName("tapActions")
    @Expose
    var tapActions: List<ActionButton> = emptyList(),

    @SerializedName("price")
    @Expose
    val price: String = "",

    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String = "",

    @SerializedName("quantity")
    @Expose
    val quantity: Int = 0,

    @SerializedName("actionButtons")
    @Expose
    var actionButtons: List<ActionButton> = emptyList(),

    @SerializedName("metaData")
    @Expose
    val metaData: String = "",

    @SerializedName("totalPrice")
    @Expose
    val totalPrice: String = "",

    @SerializedName("trackingNumber")
    @Expose
    val trackingNumber: String = "",

    @SerializedName("invoiceID")
    @Expose
    val invoiceId: String = "",

    var isTapActionsLoaded: Boolean = false,

    var isActionButtonLoaded: Boolean = false
) : Serializable {

    fun getMetaDataInfo(gson: Gson) : MetaDataInfo {
        return gson.fromJson(metaData, MetaDataInfo::class.java)
    }
}