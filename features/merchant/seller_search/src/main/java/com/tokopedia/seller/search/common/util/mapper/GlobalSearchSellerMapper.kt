package com.tokopedia.seller.search.common.util.mapper

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ALL
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.FAQ
import com.tokopedia.seller.search.common.data.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.data.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.data.SuccessSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.filter.FilterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel

object GlobalSearchSellerMapper {

    private fun mapToTitleListSearch(searchSeller: SellerSearchResponse.SellerSearch): List<String> {
        return mutableListOf<String>().apply {
            searchSeller.data.sections.map {
                add(it.title.orEmpty())
            }
        }
    }

    fun mapTopItemFilterSearch(sellerSearch: SellerSearchResponse.SellerSearch): List<FilterSearchUiModel> {
        return mutableListOf<FilterSearchUiModel>().apply {
            sellerSearch.data.sections.mapIndexed { i, section ->
                if(i.isZero()) {
                    add(FilterSearchUiModel(title = ALL, isSelected = true))
                }
                if(section.id != FAQ) {
                    if (section.has_more == true) {
                        add(FilterSearchUiModel(title = section.title, isSelected = false))
                    }
                }
            }
        }
    }

    fun mapToSellerSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch, keyword: String): List<SellerSearchUiModel> {
        return mutableListOf<SellerSearchUiModel>().apply {

            val titleList = mapToTitleListSearch(sellerSearch)

            val searchSellerList = mutableListOf<ItemSellerSearchUiModel>()
            sellerSearch.data.sections.map {
                if (it.id != FAQ) {
                    it.items.map { itemSearch ->
                        searchSellerList.add(
                                ItemSellerSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                        desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                        appUrl = itemSearch.app_url, keyword = keyword))
                    }

                    add(SellerSearchUiModel(
                            id = it.id,
                            title = it.title,
                            hasMore = it.has_more ?: false,
                            count = sellerSearch.data.count.orZero(),
                            titleList = titleList,
                            sellerSearchList = searchSellerList
                    ))
                }
            }
        }
    }

    fun mapToInitialSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<InitialSearchUiModel> {
        return mutableListOf<InitialSearchUiModel>().apply {

            val titleList = mapToTitleListSearch(sellerSearch)

            val searchSellerList = mutableListOf<ItemInitialSearchUiModel>()
            sellerSearch.data.sections.map {
                it.items.map { itemSearch ->
                    searchSellerList.add(
                            ItemInitialSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                    desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                    appUrl = itemSearch.app_url))
                }

                add(InitialSearchUiModel(
                        id = it.id,
                        title = it.title,
                        hasMore = it.has_more ?: false,
                        count = sellerSearch.data.count.orZero(),
                        titleList = titleList,
                        sellerSearchList = searchSellerList
                ))
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