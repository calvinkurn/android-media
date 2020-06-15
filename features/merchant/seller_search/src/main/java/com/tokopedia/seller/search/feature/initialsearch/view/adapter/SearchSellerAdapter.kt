package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.InitialSearchAdapterTypeFactory

class SearchSellerAdapter(
        initialSearchAdapterTypeFactory: InitialSearchAdapterTypeFactory
): BaseListAdapter<Visitable<*>, InitialSearchAdapterTypeFactory>(initialSearchAdapterTypeFactory) {

    var sellerSearchViewModel: MutableList<SellerSearchUiModel> = mutableListOf()

    fun setSellerSearchListData(sellerSearchUiModelList: List<SellerSearchUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(sellerSearchUiModelList)
        notifyItemRangeInserted(lastIndex, sellerSearchUiModelList.size)
    }

    fun removeEmptyOrErrorState() {
        when(visitables.getOrNull(lastIndex)) {
            is SellerSearchMinCharUiModel -> visitables.removeAt(lastIndex)
            is SellerSearchNoHistoryUiModel -> visitables.removeAt(lastIndex)
            is SellerSearchNoResultUiModel -> visitables.removeAt(lastIndex)
        }
    }

    fun addSellerSearchMinChar() {
        if (visitables.getOrNull(lastIndex) !is SellerSearchMinCharUiModel) {
            visitables.add(SellerSearchMinCharUiModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun addSellerSearchNoHistory() {
        if (visitables.getOrNull(lastIndex) !is SellerSearchNoHistoryUiModel) {
            visitables.add(SellerSearchNoHistoryUiModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun addSellerSearchNoResult() {
        if (visitables.getOrNull(lastIndex) !is SellerSearchNoResultUiModel) {
            visitables.add(SellerSearchNoResultUiModel())
            notifyItemInserted(lastIndex)
        }
    }
}