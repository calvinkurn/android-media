package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class PdpGetLayout(
        @SerializedName("BasicInfo")
        val basicInfo: BasicInfo = BasicInfo(),
        @SerializedName("Components")
        val components: List<Component> = listOf(),
        @SerializedName("Name")
        val generalName: String = ""
)