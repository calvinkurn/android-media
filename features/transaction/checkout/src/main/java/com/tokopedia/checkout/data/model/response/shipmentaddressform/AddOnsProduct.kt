package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class AddOnsProduct(
    @SerializedName("icon_url")
    val iconUrl: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("bottomsheet")
    val bottomsheet: AddOnsProductBottomSheet = AddOnsProductBottomSheet(),

    @SerializedName("data")
    val addOnsDataList: List<AddOnsData> = emptyList()
) {
    data class AddOnsProductBottomSheet(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("applink")
        val applink: String = "",

        @SerializedName("is_shown")
        val isShown: Boolean = false
    )

    data class AddOnsData(
        @SerializedName("id")
        val id: Long = 0L,

        @SerializedName("unique_id")
        val uniqueId: String = "",

        @SerializedName("price")
        val price: Double = 0.0,

        @SerializedName("info_link")
        val infoLink: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("status")
        val status: Int = -1,

        @SerializedName("type")
        val type: Int = -1
    )
}
