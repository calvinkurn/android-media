package com.tokopedia.autocompletecomponent.suggestion

import com.tokopedia.autocompletecomponent.suggestion.domain.model.SuggestionItem
import com.tokopedia.autocompletecomponent.util.HeadlineAdsIdList
import com.tokopedia.autocompletecomponent.util.ShopIdList

class ShopSuggestionProcessing {
    companion object {
        private const val FILTER_SHOP_COUNT = 5
        private const val CONTAINS_AT_FIRST_INDEX = 0
        private const val NOT_CONTAINS = -1
    }

    var renderedShopAds = ""
        private set

    private var excludedOrganicShop: String? = null

    fun processData(headlineAdsIdList: HeadlineAdsIdList, shopIdList: ShopIdList) {
        val mainHeadlineAdsId = headlineAdsIdList.list.firstOrNull()
        val subHeadlineAdsId = headlineAdsIdList.list.secondOrNull()

        if (mainHeadlineAdsId == null) return

        when (shopIdList.list.take(FILTER_SHOP_COUNT).indexOf(mainHeadlineAdsId)) {
            NOT_CONTAINS -> renderedShopAds = mainHeadlineAdsId
            CONTAINS_AT_FIRST_INDEX -> renderedShopAds = subHeadlineAdsId ?: ""
            else -> {
                renderedShopAds = mainHeadlineAdsId
                excludedOrganicShop = mainHeadlineAdsId
            }
        }
    }

    fun shouldSkipSuggestionItem(item: SuggestionItem): Boolean {
        return (item.type == TYPE_SHOP && item.suggestionId == excludedOrganicShop)
    }

    @Suppress("MagicNumber")
    private fun <T> List<T>.secondOrNull(): T? =
        if(this.size >= 2) this[1] else null

}