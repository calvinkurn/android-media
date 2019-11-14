package com.tokopedia.topads.data.response


import com.google.gson.annotations.SerializedName

data class ResponseGroupValidate(
        @SerializedName("data")
    val `data`: Data = Data(),
        @SerializedName("errors")
    val errors: List<Error> = listOf(),
        @SerializedName("meta")
    val meta: Meta = Meta()
) {
    data class Data(
        @SerializedName("group_name")
        val groupName: String = "",
        @SerializedName("shop_id")
        val shopId: Int = 0
    )

}