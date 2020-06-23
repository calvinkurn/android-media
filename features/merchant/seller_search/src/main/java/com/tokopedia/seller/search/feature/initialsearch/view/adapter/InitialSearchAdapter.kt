package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel

class InitialSearchAdapter(initialSearchAdapterTypeFactory: InitialSearchAdapterTypeFactory):
        BaseAdapter<InitialSearchAdapterTypeFactory>(initialSearchAdapterTypeFactory) {

    fun addAll(list: List<InitialSearchUiModel>) {
        visitables.addAll(list)
        notifyDataSetChanged()
    }

    fun removeHistory(position: Int) {
        visitables.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addMinCharState() {
        if (visitables.getOrNull(lastIndex) !is SellerSearchMinCharUiModel) {
            visitables.add(SellerSearchMinCharUiModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun addNoHistoryState() {
        if (visitables.getOrNull(lastIndex) !is SellerSearchNoHistoryUiModel) {
            visitables.add(SellerSearchNoHistoryUiModel())
            notifyItemInserted(lastIndex)
        }
    }
}
