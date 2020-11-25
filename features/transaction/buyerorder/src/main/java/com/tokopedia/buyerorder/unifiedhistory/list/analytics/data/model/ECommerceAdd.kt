package com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 13/08/20.
 */

@Parcelize
data class ECommerceAdd (
        @SerializedName("currencyCode")
        var currencyCode: String = "IDR",

        @SerializedName("add")
        var add: Add = Add()) : Parcelable {

        @Parcelize
        data class Add(
                @SerializedName("products")
                var products: ArrayList<Products> = arrayListOf()) : Parcelable {

                @Parcelize
                data class Products (
                        @SerializedName("name")
                        var name: String = "",

                        @SerializedName("id")
                        var id: String = "",

                        @SerializedName("price")
                        var price: String = "",

                        @SerializedName("brand")
                        var brand: String = "",

                        @SerializedName("category")
                        var category: String = "",

                        @SerializedName("variant")
                        var variant: String = "",

                        @SerializedName("quantity")
                        var quantity: String = "",

                        @SerializedName("dimension79")
                        var dimension79: String = "",

                        @SerializedName("dimension81")
                        var dimension81: String = "",

                        @SerializedName("dimension80")
                        var dimension80: String = "",

                        @SerializedName("dimension45")
                        var dimension45: String = "",

                        @SerializedName("dimension40")
                        var dimension40: String = ""
                ) : Parcelable
        }
}