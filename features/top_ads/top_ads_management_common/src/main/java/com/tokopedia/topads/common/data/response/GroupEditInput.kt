package com.tokopedia.topads.common.data.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Pika on 14/4/20.
 */

data class GroupEditInput(
        @field:SerializedName("action")
        var action: String = "",

        @field:SerializedName("group")
        var group: Group? = Group()
) {
    data class Group(

            @field:SerializedName("adOperations")
            var adOperations: List<AdOperationsItem>? = null,

            @field:SerializedName("priceBid")
            var priceBid: Double? = 0.0,

            @field:SerializedName("name")
            var name: String? = null,

            @field:SerializedName("priceDaily")
            var dailyBudget: Double? = 0.0,

            @field:SerializedName("scheduleEnd")
            var scheduleEnd: String? = null,

            @field:SerializedName("type")
            var type: String = "",

            @field:SerializedName("status")
            var status: String? = null,

            @field:SerializedName("scheduleStart")
            var scheduleStart: String? = ""
    ) {
        data class AdOperationsItem(

                @field:SerializedName("ad")
                var ad: Ad = Ad(),

                @field:SerializedName("action")
                var action: String = ""
        ) {
            data class Ad(

                    @field:SerializedName("productID")
                    var productId: String = ""
            )
        }
    }
}
