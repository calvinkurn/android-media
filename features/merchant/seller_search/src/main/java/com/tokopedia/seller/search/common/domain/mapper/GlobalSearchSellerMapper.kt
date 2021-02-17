package com.tokopedia.seller.search.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HIGHLIGHTS
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HISTORY
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.*
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.HighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemHighlightSuggestionSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.ItemTitleHighlightSuggestionSearchUiModel

object GlobalSearchSellerMapper {

    private fun mapToTitleListSearch(searchSeller: SellerSearchResponse.SellerSearch): List<String> {
        return mutableListOf<String>().apply {
            searchSeller.data.sections.filter { it.id != HIGHLIGHTS }.map {
                it.items.map { item ->
                    add(item.title.orEmpty())
                }
            }
        }
    }

    fun mapToSellerSearchVisitable(sellerSearch: SellerSearchResponse.SellerSearch, keyword: String): List<BaseSuggestionSearchSeller> {
        return mutableListOf<BaseSuggestionSearchSeller>().apply {
            var countItem = 0
            sellerSearch.data.sections.forEach {
                when (it.id) {
                    ORDER -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val orderSellerSearchList = mapToOrderSellerSearchVisitable(it.items, keyword, it.title.orEmpty())
                        addAll(orderSellerSearchList.first)
                        countItem += orderSellerSearchList.second
                        if (it.has_more == true) {
                            add(TitleHasMoreSellerSearchUiModel(id = it.id, title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(), actionTitle = it.action_title.orEmpty()))
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        add(DividerSellerSearchUiModel(isVisibleDivider))
                    }
                    PRODUCT -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val productSellerSearchVisitable = mapToProductSellerSearchVisitable(it.items, keyword, it.title.orEmpty())
                        addAll(productSellerSearchVisitable.first)
                        countItem += productSellerSearchVisitable.second
                        if (it.has_more == true) {
                            add(TitleHasMoreSellerSearchUiModel(id = it.id, title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(), actionTitle = it.action_title.orEmpty()))
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        add(DividerSellerSearchUiModel(isVisibleDivider))
                    }
                    NAVIGATION -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val navigationSellerSearchVisitable = mapToNavigationSellerSearchVisitable(it.items, keyword, it.title.orEmpty())
                        addAll(navigationSellerSearchVisitable.first)
                        countItem += navigationSellerSearchVisitable.second
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        add(DividerSellerSearchUiModel(isVisibleDivider))
                    }
                    FAQ -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val faqSellerSearchVisitable = mapToFaqSellerSearchVisitable(it.items, keyword, it.title.orEmpty())
                        addAll(faqSellerSearchVisitable.first)
                        if (it.has_more == true) {
                            add(TitleHasMoreSellerSearchUiModel(id = it.id, title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(), actionTitle = it.action_title.orEmpty()))
                        }
                    }
                    HIGHLIGHTS -> {
                        add(ItemTitleHighlightSuggestionSearchUiModel(it.title.orEmpty()))
                        add(HighlightSuggestionSearchUiModel(highlightSuggestionSearch = mapToItemHighlightSuggestionSearchUiModel(sellerSearch)))
                    }
                }
            }
        }
    }

    private fun mapToOrderSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                                keyword: String,
                                                title: String): Pair<List<OrderSellerSearchUiModel>, Int> {
        val orderSearchSellerList = mutableListOf<OrderSellerSearchUiModel>()
        orderSearchSellerList.apply {
            sellerSearch.map { orderItem ->
                add(OrderSellerSearchUiModel(
                        id = orderItem.id, title = orderItem.title,
                        desc = orderItem.description, imageUrl = orderItem.image_url,
                        url = orderItem.url, appUrl = orderItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
        return Pair(orderSearchSellerList, orderSearchSellerList.size)
    }

    private fun mapToProductSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                                  keyword: String,
                                                  title: String): Pair<List<ProductSellerSearchUiModel>, Int> {
        val productSellerSearchList = mutableListOf<ProductSellerSearchUiModel>()
        productSellerSearchList.apply {
            sellerSearch.map { productItem ->
                add(ProductSellerSearchUiModel(
                        id = productItem.id, title = productItem.title,
                        desc = productItem.description, imageUrl = productItem.image_url,
                        url = productItem.url, appUrl = productItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
        return Pair(productSellerSearchList, productSellerSearchList.size)
    }

    private fun mapToNavigationSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                                     keyword: String,
                                                     title: String): Pair<List<NavigationSellerSearchUiModel>, Int> {
        val navigationSellerSearchList = mutableListOf<NavigationSellerSearchUiModel>()
        navigationSellerSearchList.apply {
            sellerSearch.map { navigationItem ->
                add(NavigationSellerSearchUiModel(
                        id = navigationItem.id, title = navigationItem.title,
                        desc = navigationItem.description, imageUrl = navigationItem.image_url,
                        url = navigationItem.url, appUrl = navigationItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
        return Pair(navigationSellerSearchList, navigationSellerSearchList.size)
    }

    private fun mapToFaqSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                              keyword: String,
                                              title: String): Pair<List<FaqSellerSearchUiModel>, Int> {
        val faqSellerSearchList = mutableListOf<FaqSellerSearchUiModel>()
        faqSellerSearchList.apply {
            sellerSearch.map { faqItem ->
                add(FaqSellerSearchUiModel(
                        id = faqItem.id, title = faqItem.title,
                        desc = faqItem.description, imageUrl = faqItem.image_url,
                        url = faqItem.url, appUrl = faqItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
        return Pair(faqSellerSearchList, faqSellerSearchList.size)
    }

    fun mapToInitialSearchVisitable(sellerSearch: SellerSearchResponse.SellerSearch): Pair<List<BaseInitialSearchSeller>, List<String>> {
        val initialSearchSellerList = mutableListOf<BaseInitialSearchSeller>()
        initialSearchSellerList.apply {
            sellerSearch.data.sections.forEach {
                when (it.id) {
                    HISTORY -> {
                        add(ItemTitleInitialSearchUiModel())
                        addAll(mapToItemInitialSearchUiModel(sellerSearch))
                    }
                    HIGHLIGHTS -> {
                        add(ItemTitleHighlightInitialSearchUiModel(it.title.orEmpty()))
                        add(HighlightInitialSearchUiModel(highlightInitialList = mapToItemHighlightInitialSearchUiModel(sellerSearch)))
                    }
                }
            }
        }

        return Pair(initialSearchSellerList, mapToTitleListSearch(sellerSearch))
    }

    private fun mapToItemInitialSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<ItemInitialSearchUiModel> {
        val itemInitialSearchList = mutableListOf<ItemInitialSearchUiModel>()

        sellerSearch.data.sections.filter { it.id == HISTORY }.map { section ->
            section.items.map { itemSearch ->
                itemInitialSearchList.add(
                        ItemInitialSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                appUrl = itemSearch.app_url))
            }
        }
        return itemInitialSearchList
    }

    private fun mapToItemHighlightInitialSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<ItemHighlightInitialSearchUiModel> {
        val itemHighlightSearchList = mutableListOf<ItemHighlightInitialSearchUiModel>()

        sellerSearch.data.sections.filter { it.id == HIGHLIGHTS }.map { section ->
            section.items.map { itemSearch ->
                itemHighlightSearchList.add(
                        ItemHighlightInitialSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                appUrl = itemSearch.app_url))
            }
        }
        return itemHighlightSearchList
    }

    private fun mapToItemHighlightSuggestionSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<ItemHighlightSuggestionSearchUiModel> {
        val itemHighlightSearchList = mutableListOf<ItemHighlightSuggestionSearchUiModel>()

        sellerSearch.data.sections.filter { it.id == HIGHLIGHTS }.map { section ->
            section.items.map { itemSearch ->
                itemHighlightSearchList.add(
                        ItemHighlightSuggestionSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                appUrl = itemSearch.app_url))
            }
        }
        return itemHighlightSearchList
    }

    fun mapToDeleteHistorySearchUiModel(deleteHistory: DeleteHistoryResponse.DeleteHistory): DeleteHistorySearchUiModel {
        return DeleteHistorySearchUiModel(
                message = deleteHistory.message,
                status = deleteHistory.status
        )
    }

    fun mapToRegisterSearchUiModel(successSearchResponse: SuccessSearchResponse.SuccessSearch): RegisterSearchUiModel {
        return RegisterSearchUiModel(
                message = successSearchResponse.message,
                status = successSearchResponse.status
        )
    }
}