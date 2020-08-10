package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class PdpGetLayout(
        @SerializedName("basicInfo")
        val basicInfo: BasicInfo = BasicInfo(),
        @SerializedName("components")
        val components: List<Component> = listOf(),
        @SerializedName("name")
        val generalName: String = "",
        @SerializedName("pdpSession")
        val pdpSession: String = "",
        @SerializedName("temporaryInfo")
        val temporaryInfo: TemporaryInfo = TemporaryInfo()
)

data class TemporaryInfo(
        @SerializedName("campaignStatus")
        val campaignStatus: String = ""
)