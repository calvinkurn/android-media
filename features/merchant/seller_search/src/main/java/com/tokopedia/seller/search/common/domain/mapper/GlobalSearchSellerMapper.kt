package com.tokopedia.seller.search.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.*

object GlobalSearchSellerMapper {

    private fun mapToTitleListSearch(searchSeller: SellerSearchResponse.SellerSearch): List<String> {
        return mutableListOf<String>().apply {
            searchSeller.data.sections.map {
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
                        addAll(mapToOrderSellerSearchVisitable(it.items, keyword, it.title.orEmpty()))
                        countItem += mapToOrderSellerSearchVisitable(it.items, keyword, it.title.orEmpty()).size
                        if (it.has_more == true) {
                            add(TitleHasMoreSellerSearchUiModel(id = it.id, title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(), actionTitle = it.action_title.orEmpty()))
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        add(DividerSellerSearchUiModel(isVisibleDivider))
                    }
                    PRODUCT -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        addAll(mapToProductSellerSearchVisitable(it.items, keyword, it.title.orEmpty()))
                        countItem += mapToProductSellerSearchVisitable(it.items, keyword, it.title.orEmpty()).size
                        if (it.has_more == true) {
                            add(TitleHasMoreSellerSearchUiModel(id = it.id, title = it.action_title.orEmpty(),
                                    appActionLink = it.app_action_link.orEmpty(), actionTitle = it.action_title.orEmpty()))
                        }
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        add(DividerSellerSearchUiModel(isVisibleDivider))                    }
                    NAVIGATION -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        addAll(mapToNavigationSellerSearchVisitable(it.items, keyword, it.title.orEmpty()))
                        countItem += mapToNavigationSellerSearchVisitable(it.items, keyword, it.title.orEmpty()).size
                        val isVisibleDivider = countItem < sellerSearch.data.count.orZero()
                        add(DividerSellerSearchUiModel(isVisibleDivider))                    }
                    FAQ -> {
                        add(TitleHeaderSellerSearchUiModel(title = it.title.orEmpty()))
                        addAll(mapToFaqSellerSearchVisitable(it.items, keyword, it.title.orEmpty()))
                        if (it.has_more == true) {
                                add(TitleHasMoreSellerSearchUiModel(id = it.id, title = it.action_title.orEmpty(),
                                        appActionLink = it.app_action_link.orEmpty(), actionTitle = it.action_title.orEmpty()))
                        }
                    }
                }
            }
        }
    }

    private fun mapToOrderSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                                keyword: String,
                                                title: String): List<OrderSellerSearchUiModel> {
        return mutableListOf<OrderSellerSearchUiModel>().apply {
            sellerSearch.map { orderItem ->
                add(OrderSellerSearchUiModel(
                        id = orderItem.id, title = orderItem.title,
                        desc = orderItem.description, imageUrl = orderItem.image_url,
                        url = orderItem.url, appUrl = orderItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
    }

    private fun mapToProductSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                                  keyword: String,
                                                  title: String): List<ProductSellerSearchUiModel> {
        return mutableListOf<ProductSellerSearchUiModel>().apply {
            sellerSearch.map { productItem ->
                add(ProductSellerSearchUiModel(
                        id = productItem.id, title = productItem.title,
                        desc = productItem.description, imageUrl = productItem.image_url,
                        url = productItem.url, appUrl = productItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
    }

    private fun mapToNavigationSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                                     keyword: String,
                                                     title: String): List<NavigationSellerSearchUiModel> {
        return mutableListOf<NavigationSellerSearchUiModel>().apply {
            sellerSearch.map { navigationItem ->
                add(NavigationSellerSearchUiModel(
                        id = navigationItem.id, title = navigationItem.title,
                        desc = navigationItem.description, imageUrl = navigationItem.image_url,
                        url = navigationItem.url, appUrl = navigationItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
    }

    private fun mapToFaqSellerSearchVisitable(sellerSearch: List<SellerSearchResponse.SellerSearch.SellerSearchData.Section.Item>,
                                              keyword: String,
                                              title: String): List<FaqSellerSearchUiModel> {
        return mutableListOf<FaqSellerSearchUiModel>().apply {
            sellerSearch.map { faqItem ->
                add(FaqSellerSearchUiModel(
                        id = faqItem.id, title = faqItem.title,
                        desc = faqItem.description, imageUrl = faqItem.image_url,
                        url = faqItem.url, appUrl = faqItem.app_url, keyword = keyword,
                        section = title
                ))
            }
        }
    }

    fun mapToInitialSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): InitialSearchUiModel {

        return InitialSearchUiModel().apply {
            val titleList = mapToTitleListSearch(sellerSearch)

            val searchSellerList = mutableListOf<ItemInitialSearchUiModel>()
            sellerSearch.data.sections.map { section ->
                section.items.map { itemSearch ->
                    searchSellerList.add(
                            ItemInitialSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                    desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                    appUrl = itemSearch.app_url))
                }

                this.id = section.id
                this.title = section.title
                this.hasMore = section.has_more ?: false
                this.count = sellerSearch.data.count.orZero()
                this.titleList = titleList
                this.sellerSearchList = searchSellerList
            }
        }
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