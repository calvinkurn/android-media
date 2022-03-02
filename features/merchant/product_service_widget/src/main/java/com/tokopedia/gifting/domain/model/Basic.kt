package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Basic {
    @SerializedName("ID")
    @Expose
    var id: String = ""

    @SerializedName("Name")
    @Expose
    var name: String = ""

    @SerializedName("AddOnType")
    @Expose
    var type: String = ""

    @SerializedName("Status")
    @Expose
    var status: String = ""

    @SerializedName("OwnerWarehouseID")
    @Expose
    var ownerWarehouseID: String = ""

    @SerializedName("Rules")
    @Expose
    var rules: Rules = Rules()

    @SerializedName("Metadata")
    @Expose
    var metadata: Metadata = Metadata()
}