package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Basic {
    @SerializedName("ID")
    @Expose
    var id: String? = null

    @SerializedName("ShopID")
    @Expose
    var shopID: String? = null

    @SerializedName("Name")
    @Expose
    var name: String? = null

    @SerializedName("Type")
    @Expose
    var type: String? = null

    @SerializedName("Status")
    @Expose
    var status: String? = null

    @SerializedName("OwnerWarehouseID")
    @Expose
    var ownerWarehouseID: String? = null

    @SerializedName("IsEligible")
    @Expose
    var isEligible: Boolean? = null

    @SerializedName("Rules")
    @Expose
    var rules: Rules? = null

    @SerializedName("Metadata")
    @Expose
    var metadata: Metadata? = null
}