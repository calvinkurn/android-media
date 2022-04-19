package com.tokopedia.seller.search.feature.suggestion.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.adapter.InitialSearchDiffUtil
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel

class SuggestionSearchAdapter(typeFactory: SuggestionSearchAdapterTypeFactory):
        BaseAdapter<SuggestionSearchAdapterTypeFactory>(typeFactory) {

    fun addAll(list: List<BaseSuggestionSearchSeller>) {
        val callBack = SuggestionSearchDiffUtil(visitables, list)
        val diffResult = DiffUtil.calculateDiff(callBack)
        visitables.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addLoading() {
        if (visitables.getOrNull(lastIndex) !is LoadingSearchModel) {
            visitables.add(LoadingSearchModel())
            notifyItemInserted(lastIndex)
        }
    }

    fun addNoResultState() {
        if (visitables.getOrNull(lastIndex) !is SellerSearchNoResultUiModel) {
            visitables.add(SellerSearchNoResultUiModel())
            notifyItemInserted(lastIndex)
        }
    }
}