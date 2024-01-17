package com.tokopedia.unifyorderhistory.analytics.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by fwidjaja on 13/08/20.
 */

@Parcelize
data class ECommerceClick(
    @SerializedName("actionField")
    var actionField: ActionField = ActionField(),

    @SerializedName("products")
    var products: ArrayList<Products> = arrayListOf()
) : Parcelable {

    @Parcelize
    data class ActionField(
        @SerializedName("list")
        var list: String = ""
    ) : Parcelable

    @Parcelize
    data class Products(
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

        @SerializedName("list")
        var list: String = "",

        @SerializedName("position")
        var position: String = "",

        @SerializedName("attribution")
        var attribution: String = ""
    ) : Parcelable
}
