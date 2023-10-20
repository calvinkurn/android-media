package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GetAdProductResponse(

        @field:SerializedName("topadsGetListProductsOfGroupV2")
        val topadsGetListProductsOfGroup: TopadsGetListProductsOfGroup = TopadsGetListProductsOfGroup()
) {
    data class TopadsGetListProductsOfGroup(

        @field:SerializedName("data")
            val data: List<DataItem> = listOf(),
        @field:SerializedName("page")
            val page: Page = Page()

    ) {
        @Parcelize
        data class DataItem(

                @field:SerializedName("itemID")
                val itemID: String = "0",

                @field:SerializedName("adPriceBidFmt")
                val adPriceBidFmt: String = "",

                @field:SerializedName("groupName")
                val groupName: String = "",

                @field:SerializedName("adID")
                val adID: String = "0",

                @field:SerializedName("groupID")
                val groupID: String = "0",

                @field:SerializedName("adDetailProduct")
                val adDetailProduct: AdDetailProduct = AdDetailProduct()
        ) : Parcelable {

            @Parcelize
            data class AdDetailProduct(

                    @field:SerializedName("productURI")
                    val productURI: String = "",

                    @field:SerializedName("productImageURI")
                    val productImageURI: String = "",

                    @field:SerializedName("productName")
                    val productName: String = ""
            ) : Parcelable

        }
    }

    data class Page(
            @field:SerializedName("perPage")
            val perPage:Int = 1,
            @field:SerializedName("total")
            val total:Int = 1
    )
}

