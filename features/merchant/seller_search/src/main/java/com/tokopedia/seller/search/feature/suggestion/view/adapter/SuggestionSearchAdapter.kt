package com.tokopedia.seller.search.feature.suggestion.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.seller.search.feature.suggestion.view.model.LoadingSearchModel
import com.tokopedia.seller.search.feature.suggestion.view.model.SellerSearchNoResultUiModel
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.SellerSearchUiModel

class SuggestionSearchAdapter(typeFactory: SuggestionSearchAdapterTypeFactory):
        BaseAdapter<SuggestionSearchAdapterTypeFactory>(typeFactory) {

    fun addAll(list: List<SellerSearchUiModel>) {
        visitables.addAll(list)
        notifyDataSetChanged()
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