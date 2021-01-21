package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.model.BaseInitialSearchSeller
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel

class InitialSearchAdapter(initialSearchAdapterTypeFactory: InitialSearchAdapterTypeFactory):
        BaseAdapter<InitialSearchAdapterTypeFactory>(initialSearchAdapterTypeFactory) {

    fun addAll(list: List<BaseInitialSearchSeller>) {
        val callBack = InitialSearchDiffUtil(visitables, list)
        val diffResult = DiffUtil.calculateDiff(callBack)
        visitables.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeHistory(position: Int) {
        if(position != -1) {
            visitables.removeAt(position)
            notifyItemRemoved(position)
        }
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
