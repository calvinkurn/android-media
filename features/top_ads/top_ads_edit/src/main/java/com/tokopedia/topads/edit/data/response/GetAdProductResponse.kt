package com.tokopedia.topads.edit.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GetAdProductResponse(

        @field:SerializedName("topadsGetListProductsOfGroup")
        val topadsGetListProductsOfGroup: TopadsGetListProductsOfGroup = TopadsGetListProductsOfGroup()
) {
    data class TopadsGetListProductsOfGroup(

            @field:SerializedName("data")
            val data: List<DataItem> = listOf()

    ) {
        @Parcelize
        data class DataItem(

                @field:SerializedName("itemID")
                val itemID: Int = 0,

                @field:SerializedName("adPriceBidFmt")
                val adPriceBidFmt: String = "",

                @field:SerializedName("groupName")
                val groupName: String = "",

                @field:SerializedName("adID")
                val adID: Int = 0,

                @field:SerializedName("groupID")
                val groupID: Int = 0,

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
}

