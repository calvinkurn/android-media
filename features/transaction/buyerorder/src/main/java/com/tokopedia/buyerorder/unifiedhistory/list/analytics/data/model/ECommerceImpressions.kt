package com.tokopedia.buyerorder.unifiedhistory.list.analytics.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 13/08/20.
 */

@Parcelize
data class ECommerceImpressions (
        @SerializedName("currencyCode")
        var currencyCode: String = "IDR",

        @SerializedName("impressions")
        var impressions: ArrayList<Impressions> = arrayListOf()) : Parcelable {

        @Parcelize
        data class Impressions (
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
                var position: String = ""
        ) : Parcelable
}