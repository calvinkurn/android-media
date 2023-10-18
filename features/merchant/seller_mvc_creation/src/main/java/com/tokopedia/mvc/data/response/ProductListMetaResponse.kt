package com.tokopedia.mvc.data.response


import com.google.gson.annotations.SerializedName

data class ProductListMetaResponse(
    @SerializedName("ProductListMeta")
    val productListMeta: ProductListMeta = ProductListMeta()
) {
    data class ProductListMeta(
        @SerializedName("data")
        val `data`: Data = Data(),
        @SerializedName("header")
        val header: Header = Header(),
    ) {
        data class Data(
            @SerializedName("shopCategories")
            val shopCategories: List<ShopCategory> = listOf(),
            @SerializedName("sort")
            val sort: List<Sort> = listOf()
        ) {
            data class ShopCategory(
                @SerializedName("id")
                val id: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("value")
                val value: String = ""
            )

            data class Sort(
                @SerializedName("id")
                val id: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("value")
                val value: String = ""
            )
        }

        data class Header(
            @SerializedName("errorCode")
            val errorCode: String = "",
            @SerializedName("messages")
            val messages: List<String> = listOf(),
            @SerializedName("reason")
            val reason: String = ""
        )
    }
}
