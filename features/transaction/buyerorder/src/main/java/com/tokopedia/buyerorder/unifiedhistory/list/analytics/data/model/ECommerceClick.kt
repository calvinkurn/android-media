package com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 13/08/20.
 */

@Parcelize
data class ECommerceClick (
        @SerializedName("actionField")
        var actionField: ActionField = ActionField(),

        @SerializedName("products")
        var products: ArrayList<Products> = arrayListOf()) : Parcelable {

        @Parcelize
        data class ActionField(
                @SerializedName("list")
                var list: String = "/order list - ${UohConsts.BUSINESS_UNIT_REPLACEE}"
        ) : Parcelable

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

                @SerializedName("list")
                var list: String = "",

                @SerializedName("position")
                var position: String = "",

                @SerializedName("attribution")
                var attribution: String = ""
        ) : Parcelable
}