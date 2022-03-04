package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 02/06/20
 */
data class GetProductsByEtalaseResponse(
        @SerializedName("ProductList")
        val productList: GetProductListData
) {

        data class GetProductListData(

                @SerializedName("header")
                val header: Header = Header(),

                @SerializedName("data")
                val data: List<Data> = emptyList(),

                @SerializedName("meta")
                val meta: Meta = Meta()
        )

        data class Header(

                @SerializedName("messages")
                val messages: List<String> = emptyList(),

                @SerializedName("reason")
                val reason: String = "",

                @SerializedName("errorCode")
                val errorCode: String = ""
        )

        data class Data(

                @SerializedName("id")
                val id: String = "",

                @SerializedName("name")
                val name: String = "",

                @SerializedName("stock")
                val stock: Int = 0,

                @SerializedName("pictures")
                val pictures: List<Picture> = emptyList(),

                @SerializedName("price")
                val price: Price = Price(),
        )

        data class Picture(

                @SerializedName("urlThumbnail")
                val urlThumbnail: String = ""
        )

        data class Price(
                @SerializedName("min")
                val min: Double = 0.0,

                @SerializedName("max")
                val max: Double = 0.0,
        )

        data class Meta(

                @SerializedName("totalHits")
                val totalHits: Int = 0
        )
}