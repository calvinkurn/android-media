package com.tokopedia.topads.edit.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GetAdProductResponse(

        @field:SerializedName("topadsGetListProductsOfGroup")
        val topadsGetListProductsOfGroup: TopadsGetListProductsOfGroup = TopadsGetListProductsOfGroup()
) {
    data class TopadsGetListProductsOfGroup(

            @field:SerializedName("data")
            val data: List<DataItem> = listOf()

    ) {
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
            constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readInt(),
                    parcel.readInt(),
                    parcel.readParcelable<AdDetailProduct>(AdDetailProduct::class.java.classLoader))

            data class AdDetailProduct(

                    @field:SerializedName("productURI")
                    val productURI: String = "",

                    @field:SerializedName("productImageURI")
                    val productImageURI: String = "",

                    @field:SerializedName("productName")
                    val productName: String = ""
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                        parcel.readString(),
                        parcel.readString(),
                        parcel.readString()) {
                }

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeString(productImageURI)
                    parcel.writeString(productName)
                    parcel.writeString(productURI)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<AdDetailProduct> {
                    override fun createFromParcel(parcel: Parcel): AdDetailProduct {
                        return AdDetailProduct(parcel)
                    }

                    override fun newArray(size: Int): Array<AdDetailProduct?> {
                        return arrayOfNulls(size)
                    }
                }
            }

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(itemID)
                parcel.writeString(adPriceBidFmt)
                parcel.writeString(groupName)
                parcel.writeInt(adID)
                parcel.writeInt(groupID)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<DataItem> {
                override fun createFromParcel(parcel: Parcel): DataItem {
                    return DataItem(parcel)
                }

                override fun newArray(size: Int): Array<DataItem?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}

