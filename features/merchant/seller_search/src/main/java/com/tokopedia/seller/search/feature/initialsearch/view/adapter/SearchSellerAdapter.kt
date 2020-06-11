package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.model.sellersearch.SellerSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.InitialSearchAdapterTypeFactory

class SearchSellerAdapter(
        initialSearchAdapterTypeFactory: InitialSearchAdapterTypeFactory
): BaseListAdapter<Visitable<*>, InitialSearchAdapterTypeFactory>(initialSearchAdapterTypeFactory) {

    companion object{
        const val PAYLOAD_SUMMARY_PERIOD = 512
    }

    var sellerSearchViewModel: MutableList<SellerSearchUiModel> = mutableListOf()

    fun setProductListReviewData(sellerSearchUiModelList: List<SellerSearchUiModel>) {
        val lastIndex = visitables.size
        visitables.addAll(sellerSearchUiModelList)
        notifyItemRangeInserted(lastIndex, sellerSearchUiModelList.size)
    }
}