package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.SellerSearchMinCharViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.SellerSearchNoHistoryViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.TypeFactoryInitialSearchAdapter
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.history.HistorySearchViewHolder

class InitialSearchAdapterTypeFactory(private val historySearchListener: HistorySearchListener):
        BaseAdapterTypeFactory(), TypeFactoryInitialSearchAdapter {

    override fun type(initialSearchUiModel: ItemInitialSearchUiModel): Int {
       return HistorySearchViewHolder.LAYOUT_RES
    }

    override fun type(sellerSearchMinCharUiModel: SellerSearchMinCharUiModel): Int {
        return SellerSearchMinCharViewHolder.LAYOUT_RES
    }

    override fun type(sellerSearchNoHistoryUiModel: SellerSearchNoHistoryUiModel): Int {
        return SellerSearchNoHistoryViewHolder.LAYOUT_RES
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            HistorySearchViewHolder.LAYOUT_RES -> HistorySearchViewHolder(parent, historySearchListener)
            SellerSearchNoHistoryViewHolder.LAYOUT_RES -> SellerSearchNoHistoryViewHolder(parent)
            SellerSearchMinCharViewHolder.LAYOUT_RES -> SellerSearchMinCharViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}