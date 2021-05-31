package com.tokopedia.seller.search.feature.initialsearch.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchMinCharUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.SellerSearchNoHistoryUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleHighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.ItemTitleInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.*
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight.HighlightInitialSearchViewHolder
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight.TitleHighlightInitialSearchViewHolder
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

    override fun type(itemTitleHighlightInitialSearchUiModel: ItemTitleHighlightInitialSearchUiModel): Int {
        return TitleHighlightInitialSearchViewHolder.LAYOUT
    }

    override fun type(highlightInitialSearchUiModel: HighlightInitialSearchUiModel): Int {
        return HighlightInitialSearchViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            HistorySearchViewHolder.LAYOUT_RES -> HistorySearchViewHolder(parent, historySearchListener)
            SellerSearchNoHistoryViewHolder.LAYOUT_RES -> SellerSearchNoHistoryViewHolder(parent)
            SellerSearchMinCharViewHolder.LAYOUT_RES -> SellerSearchMinCharViewHolder(parent)
            TitleHighlightInitialSearchViewHolder.LAYOUT -> TitleHighlightInitialSearchViewHolder(parent)
            TitleInitialSearchViewHolder.LAYOUT -> TitleInitialSearchViewHolder(parent, historySearchListener)
            HighlightInitialSearchViewHolder.LAYOUT -> HighlightInitialSearchViewHolder(parent, historySearchListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}