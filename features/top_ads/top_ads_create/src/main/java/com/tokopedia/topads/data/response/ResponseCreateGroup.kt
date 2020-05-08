package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

data class ResponseCreateGroup(

        @field:SerializedName("topadsCreateGroupAds")
        var topadsCreateGroupAds: TopadsCreateGroupAds = TopadsCreateGroupAds()
) {

    data class TopadsCreateGroupAds(

            @field:SerializedName("data")
            var data: Data = Data(),

            @field:SerializedName("meta")
            var meta: Meta = Meta(),

            @field:SerializedName("errors")
            var errors: List<Error> = listOf()
    ) {
        data class Data(

                @field:SerializedName("keywords")
                var keywords: List<KeywordsItem> = listOf(),

                @field:SerializedName("group")
                var group: Group = Group()
        )
    }

    data class Meta(

            @field:SerializedName("messages")
            var messages: List<MessagesItem> = listOf()
    )


    data class Error(
            var code: String = "",
            var detail: String = "",
            var title: String = ""
    )

    data class MessagesItem(

            @field:SerializedName("code")
            var code: String = "",

            @field:SerializedName("detail")
            var detail: String = "",

            @field:SerializedName("title")
            var title: String = ""
    )

    data class AdsItem(

            @field:SerializedName("adImage")
            var adImage: String = "",

            @field:SerializedName("productURI")
            var productURI: String = "",

            @field:SerializedName("ad")
            var ad: Ad = Ad(),

            @field:SerializedName("productID")
            var productID: String = "",

            @field:SerializedName("adStatusToogle")
            var adStatusToogle: Int = 0,

            @field:SerializedName("isEnoughDeposit")
            var isEnoughDeposit: Boolean = false,

            @field:SerializedName("groupID")
            var groupID: String = "",

            @field:SerializedName("adTitle")
            var adTitle: String = "",

            @field:SerializedName("adSchedule")
            var adSchedule: String = "",

            @field:SerializedName("adEndTime")
            var adEndTime: String = "",

            @field:SerializedName("toggle")
            var toggle: String = "",

            @field:SerializedName("source")
            var source: String = "",

            @field:SerializedName("adEndDate")
            var adEndDate: String = "",

            @field:SerializedName("stickerID")
            var stickerID: String = "",

            @field:SerializedName("adStartTime")
            var adStartTime: String = "",

            @field:SerializedName("adStartDate")
            var adStartDate: String = "",

            @field:SerializedName("shopID")
            var shopID: String = ""
    )


    data class Ad(

            @field:SerializedName("statusDesc")
            var statusDesc: String = "",

            @field:SerializedName("isSuggestionBidvarue")
            var isSuggestionBidvarue: String = "",

            @field:SerializedName("adBudget")
            var adBudget: String = "",

            @field:SerializedName("priceBid")
            var priceBid: Int = 0,

            @field:SerializedName("suggestedBidvarue")
            var suggestedBidvarue: Int = 0,

            @field:SerializedName("previousBid")
            var previousBid: Int = 0,

            @field:SerializedName("adPriceDailyFmt")
            var adPriceDailyFmt: String = "",

            @field:SerializedName("adPriceBidFmt")
            var adPriceBidFmt: String = "",

            @field:SerializedName("adType")
            var adType: String = "1",

            @field:SerializedName("adID")
            var adID: String = "0",

            @field:SerializedName("priceDaily")
            var priceDaily: Int = 0,

            @field:SerializedName("isSuggestionBidButton")
            var isSuggestionBidButton: String = "",

            @field:SerializedName("status")
            var status: String = ""
    )

    data class Group(

            @field:SerializedName("groupType")
            var groupType: String = "",

            @field:SerializedName("groupSchedule")
            var groupSchedule: String = "",

            @field:SerializedName("source")
            var source: String = "",

            @field:SerializedName("previousBid")
            var previousBid: Int = 0,

            @field:SerializedName("groupTotal")
            var groupTotal: String = "",

            @field:SerializedName("priceDaily")
            var priceDaily: Int = 0,

            @field:SerializedName("groupEndTime")
            var groupEndTime: String = "",

            @field:SerializedName("groupStartTime")
            var groupStartTime: String = "",

            @field:SerializedName("isCreateAff")
            var isCreateAff: Boolean = false,

            @field:SerializedName("isAutoAds")
            var isAutoAds: Boolean = false,

            @field:SerializedName("isSuggestionBidButton")
            var isSuggestionBidButton: String = "",

            @field:SerializedName("statusDesc")
            var statusDesc: String = "",

            @field:SerializedName("groupBudget")
            var groupBudget: String = "",

            @field:SerializedName("isSuggestionBidvarue")
            var isSuggestionBidvarue: String = "",

            @field:SerializedName("isEnoughDeposit")
            var isEnoughDeposit: Boolean = false,

            @field:SerializedName("groupID")
            var groupID: String = "",

            @field:SerializedName("departmentID")
            var departmentID: String = "",

            @field:SerializedName("oldPriceBid")
            var oldPriceBid: Int = 0,

            @field:SerializedName("toggle")
            var toggle: String = "",

            @field:SerializedName("priceBid")
            var priceBid: Int = 0,

            @field:SerializedName("suggestedBidvarue")
            var suggestedBidvarue: Int = 0,

            @field:SerializedName("isCreateAds")
            var isCreateAds: Boolean = false,

            @field:SerializedName("stickerID")
            var stickerID: String = "",

            @field:SerializedName("ads")
            var ads: List<AdsItem> = listOf(),

            @field:SerializedName("groupName")
            var groupName: String = "",

            @field:SerializedName("groupStartDate")
            var groupStartDate: String = "",

            @field:SerializedName("groupEndDate")
            var groupEndDate: String = "",

            @field:SerializedName("keywordTotal")
            var keywordTotal: String = "",

            @field:SerializedName("shopID")
            var shopID: String = "",

            @field:SerializedName("status")
            var status: String = ""
    )

    data class KeywordsItem(

            @field:SerializedName("customBid")
            var customBid: Int = 0,

            @field:SerializedName("keywordTag")
            var keywordTag: String = "",

            @field:SerializedName("keywordTypeID")
            var keywordTypeID: String = "",

            @field:SerializedName("isEnoughDeposit")
            var isEnoughDeposit: Boolean = false,

            @field:SerializedName("groupID")
            var groupID: String = "",

            @field:SerializedName("toggle")
            var toggle: String = "",

            @field:SerializedName("shopID")
            var shopID: String = "",

            @field:SerializedName("source")
            var source: String = "",

            @field:SerializedName("priceBid")
            var priceBid: Int = 0,

            @field:SerializedName("status")
            var status: String = ""
    )
}
