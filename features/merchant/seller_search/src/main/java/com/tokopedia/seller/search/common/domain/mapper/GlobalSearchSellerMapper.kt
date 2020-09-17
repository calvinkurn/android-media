package com.tokopedia.seller.search.common.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.NAVIGATION
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ORDER
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.PRODUCT
import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.OrderSellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.TitleHasMoreSellerSearchUiModel

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

    fun mapToSellerSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch, keyword: String): List<SellerSearchUiModel> {
        return mutableListOf<SellerSearchUiModel>().apply {

            val titleList = mapToTitleListSearch(sellerSearch)

            sellerSearch.data.sections.map {
                val searchSellerList = mutableListOf<ItemSellerSearchUiModel>()
                it.items.map { itemSearch ->
                    searchSellerList.add(
                            ItemSellerSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                    desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                    url = itemSearch.url, appUrl = itemSearch.app_url, keyword = keyword,
                                    section = it.title))
                }

                add(SellerSearchUiModel(
                        id = it.id,
                        title = it.title,
                        hasMore = it.has_more ?: false,
                        count = sellerSearch.data.count.orZero(),
                        actionTitle = it.action_title,
                        actionLink = it.action_link,
                        appActionLink = it.app_action_link,
                        titleList = titleList,
                        sellerSearchList = searchSellerList
                ))
            }
        }
    }

    private fun mapToSellerSearchVisitable(sellerSearch: SellerSearchResponse.SellerSearch, keyword: String): List<BaseInitialSearchSeller> {
        return mutableListOf<BaseInitialSearchSeller>().apply {
            sellerSearch.data.sections.map {
                when(it.id) {
                    ORDER -> {
                        add(TitleHasMoreSellerSearchUiModel(title = it.title.orEmpty()))
                        add()
                        add(TitleHasMoreSellerSearchUiModel(it.action_title.orEmpty()))
                    }
                    PRODUCT -> {

                    }
                    NAVIGATION -> {

                    }
                    FAQ -> {

                    }
                    else -> {}
                }
            }
        }
    }

    private fun mapToProductSellerSearchVisitable(sellerSearch: SellerSearchResponse.SellerSearch, keyword: String): OrderSellerSearchUiModel {
        
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