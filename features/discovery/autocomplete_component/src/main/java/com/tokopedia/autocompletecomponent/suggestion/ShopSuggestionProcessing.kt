package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocompletecomponent.util.HeadlineAdsIdList
import com.tokopedia.autocompletecomponent.util.SuggestionItemIdList

class ShopSuggestionProcessing {
    companion object {
        private const val FILTER_SHOP_COUNT = 5
        private const val CONTAINS_AT_FIRST_INDEX = 0
        private const val NOT_CONTAINS = -1
    }

    var renderedShopAdsId = ""
        private set

    private var excludedOrganicShopId: String = ""

    fun processData(headlineAdsIdList: HeadlineAdsIdList, suggestionItemIdList: SuggestionItemIdList) {
        val mainHeadlineAdsId = headlineAdsIdList.list.firstOrNull()
        val subHeadlineAdsId = headlineAdsIdList.list.secondOrNull()
        val nonEmptySuggestionItemIdList = suggestionItemIdList.list.filter { it.isNotEmpty() }

        if (mainHeadlineAdsId == null) return

        when (nonEmptySuggestionItemIdList.take(FILTER_SHOP_COUNT).indexOf(mainHeadlineAdsId)) {
            NOT_CONTAINS -> renderedShopAdsId = mainHeadlineAdsId
            CONTAINS_AT_FIRST_INDEX -> renderedShopAdsId = subHeadlineAdsId ?: ""
            else -> {
                renderedShopAdsId = mainHeadlineAdsId
                excludedOrganicShopId = mainHeadlineAdsId
            }
        }
    }

    fun shouldSkipSuggestionItem(item: SuggestionItem): Boolean {
        return item.hasSuggestionId() && item.suggestionId == excludedOrganicShopId
    }

    @Suppress("MagicNumber")
    private fun <T> List<T>.secondOrNull(): T? =
        if(this.size >= 2) this[1] else null

    private fun SuggestionItem.hasSuggestionId(): Boolean = suggestionId.isNotEmpty()
}