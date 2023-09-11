package com.tokopedia.addon.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.gifting.domain.model.Metadata


data class Basic (
    @SerializedName("ID")
    var basicId: String = "",

    @SerializedName("ShopID")
    var shopID: String = "",

    @SerializedName("Name")
    var name: String = "",

    @SerializedName("AddOnType")
    var addOnType: String = "",

    @SerializedName("Status")
    var status: String = "",

    @SerializedName("IsEligible")
    var isEligible: Boolean = false,

    @SerializedName("OwnerWarehouseID")
    var ownerWarehouseID: String = "",

    @SerializedName("Metadata")
    var metadata: Metadata = Metadata(),

    @SerializedName("Rules")
    var rules: Rules = Rules(),

    @SerializedName("AddOnKey")
    var addOnKey: String = "",
)
