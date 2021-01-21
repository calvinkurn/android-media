package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight.HighlightSearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight.TitleHighlightSearchViewHolder
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

    override fun type(itemTitleInitialSearchUiModel: ItemTitleInitialSearchUiModel): Int {
        return TitleInitialSearchViewHolder.LAYOUT
    }

    override fun type(itemTitleHighlightSearchUiModel: ItemTitleHighlightSearchUiModel): Int {
        return TitleHighlightSearchViewHolder.LAYOUT
    }

    override fun type(highlightSearchUiModel: HighlightSearchUiModel): Int {
        return HighlightSearchViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            HistorySearchViewHolder.LAYOUT_RES -> HistorySearchViewHolder(parent, historySearchListener)
            SellerSearchNoHistoryViewHolder.LAYOUT_RES -> SellerSearchNoHistoryViewHolder(parent)
            SellerSearchMinCharViewHolder.LAYOUT_RES -> SellerSearchMinCharViewHolder(parent)
            TitleHighlightSearchViewHolder.LAYOUT -> TitleHighlightSearchViewHolder(parent)
            TitleInitialSearchViewHolder.LAYOUT -> TitleInitialSearchViewHolder(parent, historySearchListener)
            HighlightSearchViewHolder.LAYOUT -> HighlightSearchViewHolder(parent, historySearchListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}