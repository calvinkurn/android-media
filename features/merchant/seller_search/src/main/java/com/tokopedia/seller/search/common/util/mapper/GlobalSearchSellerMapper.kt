package com.tokopedia.seller.search.common.util.mapper

import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller.search.common.GlobalSearchSellerConstant.ALL
import com.tokopedia.seller.search.common.data.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.data.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.data.SuccessSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.filter.FilterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.ItemSellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel

object GlobalSearchSellerMapper {

    fun mapTopItemFilterSearch(sellerSearch: SellerSearchResponse.SellerSearch): List<FilterSearchUiModel> {
        return mutableListOf<FilterSearchUiModel>().apply {
            sellerSearch.data.sections.mapIndexed { i, section ->
                if(i.isZero()) {
                    add(FilterSearchUiModel(title = ALL, isSelected = true))
                }
                if(section.has_more == true) {
                    add(FilterSearchUiModel(title = section.title, isSelected = false))
                }
            }
        }
    }

    fun mapToSellerSearchUiModel(sellerSearch: SellerSearchResponse.SellerSearch): List<SellerSearchUiModel> {
        return mutableListOf<SellerSearchUiModel>().apply {
            val searchSellerList = mutableListOf<ItemSellerSearchUiModel>()
            sellerSearch.data.sections.map {
                it.items.map { itemSearch ->
                    searchSellerList.add(
                            ItemSellerSearchUiModel(id = itemSearch.id, title = itemSearch.title,
                                    desc = itemSearch.description, imageUrl = itemSearch.image_url,
                                    appUrl = itemSearch.app_url))
                }

                add(SellerSearchUiModel(
                        id = it.id,
                        title = it.title,
                        hasMore = it.has_more ?: false,
                        count = sellerSearch.data.count.orZero(),
                        sellerSearchList = searchSellerList
                ))
            }
        }

    }

    fun mapToDeleteHistorySearchUiModel(deleteHistoryResponse: DeleteHistoryResponse): DeleteHistorySearchUiModel {
        return DeleteHistorySearchUiModel(
                message = deleteHistoryResponse.message,
                status = deleteHistoryResponse.status
        )
    }

    fun mapToRegisterSearchUiModel(successSearchResponse: SuccessSearchResponse): RegisterSearchUiModel {
        return RegisterSearchUiModel(
                message = successSearchResponse.message,
                status = successSearchResponse.status
        )
    }
}