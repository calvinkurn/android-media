package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.Field

/**
 * Created by Pika on 14/4/20.
 */

data class GroupEditInput(
        @field:SerializedName("action")
        var action: String = "",

        @field:SerializedName("group")
        var group: Group? = Group()
) {
    @Parcelize
    data class Group(

            @field:SerializedName("type")
            var type: String = "",

            @field:SerializedName("adOperations")
            var adOperations: List<AdOperationsItem>? = null,

            @field:SerializedName("name")
            var name: String? = null,

            @field:SerializedName("dailyBudget")
            var dailyBudget: Double? = 0.0,

            @field:SerializedName("bidSettings")
            var bidSettings: List<TopadsGroupBidSetting>? = arrayListOf(),

            @field:SerializedName("status")
            var status: String? = null,

            @field:SerializedName("strategies")
            var strategies: ArrayList<String>? = arrayListOf(),

            @field:SerializedName("suggestionBidSettings")
            var suggestionBidSettings : List<TopadsSuggestionBidSetting>? = null
    )  : Parcelable {
        @Parcelize
        data class TopadsSuggestionBidSetting(
            @field:SerializedName("bidType")
            var bidType: String? = "",
            @field:SerializedName("suggestionPriceBid")
            var suggestionPriceBid: Float = 0f,
        ) : Parcelable

        @Parcelize
        data class TopadsGroupBidSetting(
            @field:SerializedName("bidType")
            var bidType: String? = null,

            @field:SerializedName("priceBid")
            var priceBid: Float? = 0.0f

        ) : Parcelable
        @Parcelize
        data class AdOperationsItem(

                @field:SerializedName("ad")
                var ad: Ad = Ad(),

                @field:SerializedName("action")
                var action: String = ""
        ) : Parcelable {
            @Parcelize
            data class Ad(

                    @field:SerializedName("productID")
                    var productId: String = ""
            ) : Parcelable
        }
    }
}
