package com.tokopedia.topads.common.data.model

data class InsightTypeApplyInput(
    var insightType: String = "0",
    var groupId: String = "",
    val keywordList: MutableList<KeywordInput> = mutableListOf(),
    val groupOperation: GroupOperation
) {
    data class KeywordInput(
        var action: String = "",
        var keywordId: String = "",
        var keywordType: String = "",
        var keywordStatus: String = "",
        var keywordTag: String = "",
        var keywordSource: String = "",
        // for kata kuchi(newPositiveKeywordsRecom)  use suggestionBid
        var priceBid: Double = 0.0,
        var suggestionPriceBid: Double = 0.0
        // //////////////////////////////////
    )

    data class GroupOperation(
        var action: String = "",
        var bidSettings: BidSettings,
        var suggestionBidSettings: SuggestionBidSettings

    ) {
        class BidSettings

        class SuggestionBidSettings
    }
}
