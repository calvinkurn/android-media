package com.tokopedia.seller.search.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ARTICLES
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HIGHLIGHTS
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.HISTORY
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ArticleSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.DividerSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.FaqSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchSubItemUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.NavigationSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ProductSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHeaderSellerSearchUiModel
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

    fun mapToSellerSearchVisitable(
        sellerSearch: SellerSearchResponse.SellerSearch,
        keyword: String
    ): List<BaseSuggestionSearchSeller> {
        return mutableListOf<BaseSuggestionSearchSeller>().apply {
            var countItem = 0
            sellerSearch.data.sections.forEach {
                when (it.id) {
                    ORDER -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val (orderSellerSearchList, itemCount) = mapToOrderSellerSearchVisitable(
                            it.items,
                            keyword,
                            it.title.orEmpty()
                        )
                        addAll(orderSellerSearchList)
                        countItem += itemCount
                        if (it.has_more == true) {
                            add(
                                TitleHasMoreSellerSearchUiModel(
                                    id = it.id,
                                    title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(),
                                    actionTitle = it.action_title.orEmpty()
                                )
                            )
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        if (isVisibleDivider) {
                            add(DividerSellerSearchUiModel())
                        }
                    }

                    PRODUCT -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val (productSellerSearchVisitable, itemCount) = mapToProductSellerSearchVisitable(
                            it.items,
                            keyword,
                            it.title.orEmpty()
                        )
                        addAll(productSellerSearchVisitable)
                        countItem += itemCount
                        if (it.has_more == true) {
                            add(
                                TitleHasMoreSellerSearchUiModel(
                                    id = it.id,
                                    title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(),
                                    actionTitle = it.action_title.orEmpty()
                                )
                            )
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        if (isVisibleDivider) {
                            add(DividerSellerSearchUiModel())
                        }
                    }

                    NAVIGATION -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val (navigationSellerSearchVisitable, itemCount) = mapToNavigationSellerSearchVisitable(
                            it.items,
                            keyword,
                            it.title.orEmpty()
                        )
                        addAll(navigationSellerSearchVisitable)
                        countItem += itemCount
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        if (isVisibleDivider) {
                            add(DividerSellerSearchUiModel())
                        }
                    }

                    ARTICLES -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val (articleSellerSearchVisitable, itemCount) = mapToArticleSellerSearchVisitable(
                            it.items,
                            keyword,
                            it.title.orEmpty()
                        )
                        addAll(articleSellerSearchVisitable)
                        countItem += itemCount
                        if (it.has_more == true) {
                            add(
                                TitleHasMoreSellerSearchUiModel(
                                    id = it.id,
                                    title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(),
                                    actionTitle = it.action_title.orEmpty()
                                )
                            )
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        if (isVisibleDivider) {
                            add(DividerSellerSearchUiModel())
                        }
                    }

                    FAQ -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        val (faqSellerSearchVisitable, itemCount) = mapToFaqSellerSearchVisitable(
                            it.items,
                            keyword,
                            it.title.orEmpty()
                        )
                        addAll(faqSellerSearchVisitable)
                        countItem += itemCount
                        if (it.has_more == true) {
                            add(
                                TitleHasMoreSellerSearchUiModel(
                                    id = it.id,
                                    title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(),
                                    actionTitle = it.action_title.orEmpty()
                                )
                            )
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        if (isVisibleDivider) {
                            add(DividerSellerSearchUiModel())
                        }
                    }

                    HIGHLIGHTS -> {
                        add(ItemTitleHighlightSuggestionSearchUiModel(it.title.orEmpty()))
                        add(
                            HighlightSuggestionSearchUiModel(
                                highlightSuggestionSearch = mapToItemHighlightSuggestionSearchUiModel(
                                    sellerSearch
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    fun mapToSuggestionSellerSearchVisitable(
        sellerSearchVisitableList: List<BaseSuggestionSearchSeller>
    ): List<BaseSuggestionSearchSeller> {
        val suggestionSellerSearchList = mutableListOf<BaseSuggestionSearchSeller>()

        val highlightsData = sellerSearchVisitableList.filterIsInstance<HighlightSuggestionSearchUiModel>()
        if (highlightsData.isNotEmpty()) {
            suggestionSellerSearchList.add(SellerSearchNoResultUiModel())
            val itemTitleHighlightSearchUiModel =
                sellerSearchVisitableList.filterIsInstance<ItemTitleHighlightSuggestionSearchUiModel>().firstOrNull()
                    ?: ItemTitleHighlightSuggestionSearchUiModel()
            val itemHighlightSearchUiModel =
                highlightsData.firstOrNull() ?: HighlightSuggestionSearchUiModel()
            val highlightSearchVisitable =
                mutableListOf(itemTitleHighlightSearchUiModel, itemHighlightSearchUiModel)
            suggestionSellerSearchList.addAll(highlightSearchVisitable)
        } else {
            suggestionSellerSearchList.addAll(sellerSearchVisitableList)
        }
        return suggestionSellerSearchList.toList()
    }

    private fun mapToOrderSellerSearchVisitable(
        sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
        keyword: String,
        title: String
    ): Pair<List<OrderSellerSearchUiModel>, Int> {
        val orderSearchSellerList = mutableListOf<OrderSellerSearchUiModel>()
        orderSearchSellerList.apply {
            sellerSearch.map { orderItem ->
                add(
                    OrderSellerSearchUiModel(
                        id = orderItem.id,
                        title = orderItem.title,
                        desc = orderItem.description,
                        url = orderItem.url,
                        appUrl = orderItem.app_url,
                        keyword = keyword,
                        section = title
                    )
                )
            }
        }
        return Pair(orderSearchSellerList, orderSearchSellerList.size)
    }

    private fun mapToProductSellerSearchVisitable(
        sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
        keyword: String,
        title: String
    ): Pair<List<ProductSellerSearchUiModel>, Int> {
        val productSellerSearchList = mutableListOf<ProductSellerSearchUiModel>()
        productSellerSearchList.apply {
            sellerSearch.map { productItem ->
                add(
                    ProductSellerSearchUiModel(
                        id = productItem.id,
                        title = productItem.title,
                        desc = productItem.description,
                        imageUrl = productItem.image_url,
                        url = productItem.url,
                        appUrl = productItem.app_url,
                        keyword = keyword,
                        section = title
                    )
                )
            }
        }
        return Pair(productSellerSearchList, productSellerSearchList.size)
    }

    private fun mapToArticleSellerSearchVisitable(
        sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
        keyword: String,
        title: String
    ): Pair<List<ArticleSellerSearchUiModel>, Int> {
        val articleSellerSearchList = mutableListOf<ArticleSellerSearchUiModel>()
        articleSellerSearchList.apply {
            sellerSearch.map { articleItem ->
                add(
                    ArticleSellerSearchUiModel(
                        id = articleItem.id,
                        title = articleItem.title,
                        desc = articleItem.description,
                        imageUrl = articleItem.image_url,
                        url = articleItem.url,
                        appUrl = articleItem.app_url,
                        keyword = keyword,
                        section = title
                    )
                )
            }
        }
        return Pair(articleSellerSearchList, articleSellerSearchList.size)
    }

    private fun mapToNavigationSellerSearchVisitable(
        sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
        keyword: String,
        title: String
    ): Pair<List<NavigationSellerSearchUiModel>, Int> {
        val navigationSellerSearchList = mutableListOf<NavigationSellerSearchUiModel>()
        navigationSellerSearchList.run {
            sellerSearch.map { navigationItem ->
                add(
                    NavigationSellerSearchUiModel(
                        id = navigationItem.id,
                        title = navigationItem.title,
                        desc = navigationItem.description,
                        imageUrl = navigationItem.image_url,
                        url = navigationItem.url,
                        appUrl = navigationItem.app_url,
                        keyword = keyword,
                        section = title,
                        subItems = navigationItem.subItems.map { subItem ->
                            NavigationSellerSearchSubItemUiModel(
                                title = subItem.title.orEmpty(),
                                appLink = subItem.appUrl.orEmpty()
                            )
                        }
                    )
                )
            }
        }
        return Pair(navigationSellerSearchList, navigationSellerSearchList.size)
    }

    private fun mapToFaqSellerSearchVisitable(
        sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
        keyword: String,
        title: String
    ): Pair<List<FaqSellerSearchUiModel>, Int> {
        val faqSellerSearchList = mutableListOf<FaqSellerSearchUiModel>()
        faqSellerSearchList.apply {
            sellerSearch.map { faqItem ->
                add(
                    FaqSellerSearchUiModel(
                        id = faqItem.id,
                        title = faqItem.title,
                        desc = faqItem.description,
                        imageUrl = faqItem.image_url,
                        url = faqItem.url,
                        appUrl = faqItem.app_url,
                        keyword = keyword,
                        section = title
                    )
                )
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
                        add(
                            HighlightInitialSearchUiModel(
                                highlightInitialList = mapToItemHighlightInitialSearchUiModel(
                                    sellerSearch
                                )
                            )
                        )
                    }
                }
            }
        }

        return Pair(initialSearchSellerList, mapToTitleListSearch(sellerSearch))
    }

    fun mapToInitialSearchVisitableCompose(sellerSearch: SellerSearchResponse.SellerSearch): Pair<List<BaseInitialSearchSeller>, List<String>> {
        val initialSearchSellerList = mutableListOf<BaseInitialSearchSeller>()
        initialSearchSellerList.run {
            val historyList = sellerSearch.data.sections.filter { it.id == HISTORY }

            sellerSearch.data.sections.forEach {
                when (it.id) {
                    HISTORY -> {
                        add(ItemTitleInitialSearchUiModel())
                        addAll(mapToItemInitialSearchUiModel(sellerSearch))
                    }

                    HIGHLIGHTS -> {
                        if (historyList.isEmpty()) {
                            add(SellerSearchNoHistoryUiModel())
                        }
                        add(ItemTitleHighlightInitialSearchUiModel(it.title.orEmpty()))
                        add(
                            HighlightInitialSearchUiModel(
                                highlightInitialList = mapToItemHighlightInitialSearchUiModel(
                                    sellerSearch
                                )
                            )
                        )
                    }
                }
            }
        }

        return Pair(initialSearchSellerList, mapToTitleListSearch(sellerSearch))
    }

    fun mapToDeleteAllSuggestionSearch(
        initialSearchSellerList: List<BaseInitialSearchSeller>
    ): List<BaseInitialSearchSeller> {
        val initialSearchSellerMutableList = mutableListOf<BaseInitialSearchSeller>().apply {
            addAll(initialSearchSellerList)
        }

        initialSearchSellerMutableList.removeItemTitleInitial()

        initialSearchSellerMutableList.removeAll { it is ItemInitialSearchUiModel }

        initialSearchSellerMutableList.add(Int.ZERO, SellerSearchNoHistoryUiModel())

        return initialSearchSellerMutableList.toList()
    }

    fun mapToDeleteItemSuggestionSearch(
        initialSearchSellerList: List<BaseInitialSearchSeller>,
        itemPosition: Int
    ): List<BaseInitialSearchSeller> {
        val initialSearchSellerMutableList = mutableListOf<BaseInitialSearchSeller>().apply {
            addAll(initialSearchSellerList)
        }

        if (itemPosition > -Int.ONE) {
            initialSearchSellerMutableList.removeAt(itemPosition)
        }

        val itemInitialStateList = initialSearchSellerMutableList.filterIsInstance<ItemInitialSearchUiModel>()

        if (itemInitialStateList.isEmpty()) {
            initialSearchSellerMutableList.removeItemTitleInitial()
            initialSearchSellerMutableList.add(Int.ZERO, SellerSearchNoHistoryUiModel())
        }

        return initialSearchSellerMutableList.toList()
    }

    private fun MutableList<BaseInitialSearchSeller>.removeItemTitleInitial() {
        val indexItemTitleInitial = indexOfFirst { it is ItemTitleInitialSearchUiModel }
        if (indexItemTitleInitial > -Int.ONE) {
            this.removeAt(indexItemTitleInitial)
        }
    }

    private fun mapToItemInitialSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<ItemInitialSearchUiModel> {
        val itemInitialSearchList = mutableListOf<ItemInitialSearchUiModel>()

        sellerSearch.data.sections.filter { it.id == HISTORY }.map { section ->
            section.items.map { itemSearch ->
                itemInitialSearchList.add(
                    ItemInitialSearchUiModel(
                        id = itemSearch.id,
                        title = itemSearch.title,
                        desc = itemSearch.description,
                        imageUrl = itemSearch.image_url,
                        appUrl = itemSearch.app_url
                    )
                )
            }
        }
        return itemInitialSearchList
    }

    private fun mapToItemHighlightInitialSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<ItemHighlightInitialSearchUiModel> {
        val itemHighlightSearchList = mutableListOf<ItemHighlightInitialSearchUiModel>()

        sellerSearch.data.sections.filter { it.id == HIGHLIGHTS }.map { section ->
            section.items.map { itemSearch ->
                itemHighlightSearchList.add(
                    ItemHighlightInitialSearchUiModel(
                        id = itemSearch.id,
                        title = itemSearch.title,
                        desc = itemSearch.description,
                        imageUrl = itemSearch.image_url,
                        appUrl = itemSearch.app_url
                    )
                )
            }
        }
        return itemHighlightSearchList
    }

    private fun mapToItemHighlightSuggestionSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<ItemHighlightSuggestionSearchUiModel> {
        val itemHighlightSearchList = mutableListOf<ItemHighlightSuggestionSearchUiModel>()

        sellerSearch.data.sections.filter { it.id == HIGHLIGHTS }.map { section ->
            section.items.map { itemSearch ->
                itemHighlightSearchList.add(
                    ItemHighlightSuggestionSearchUiModel(
                        id = itemSearch.id,
                        title = itemSearch.title,
                        desc = itemSearch.description,
                        imageUrl = itemSearch.image_url,
                        appUrl = itemSearch.app_url
                    )
                )
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
