package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.product.changeview.ChangeViewListener
import com.tokopedia.search.result.product.changeview.ViewType
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.ListInspirationKeywordItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.SmallInspirationKeywordItemViewHolder

class InspirationKeywordsTypeFactoryImpl(
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val changeViewListener: ChangeViewListener,
    private val columnQty: Int,
    private val isNoImageCard: Boolean
) :
    BaseAdapterTypeFactory(), InspirationKeywordsTypeFactory {
    override fun type(item: InspirationKeywordDataView): Int {
        return when (changeViewListener.viewType) {
            ViewType.LIST, ViewType.BIG_GRID ->
                ListInspirationKeywordItemViewHolder.LAYOUT
            ViewType.SMALL_GRID ->
                SmallInspirationKeywordItemViewHolder.LAYOUT
        }
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when {
            type.isListInspirationKeywordItemViewHolder() && isListView() ->
                ListInspirationKeywordItemViewHolder(
                    view,
                    inspirationKeywordListener,
                    columnQty.isNotEvenNumber()
                )
            type.isListInspirationKeywordItemViewHolder() && isBigGridView() ->
                ListInspirationKeywordItemViewHolder(
                    view,
                    inspirationKeywordListener,
                    true
                )
            type.isSmallInspirationKeywordItemViewHolder() && isSmallGridView() ->
                SmallInspirationKeywordItemViewHolder(view, inspirationKeywordListener)
            else -> super.createViewHolder(view, type)
        }
    }

    private fun isListView() = changeViewListener.viewType.value == ViewType.LIST.value
    private fun isBigGridView() = changeViewListener.viewType.value == ViewType.BIG_GRID.value
    private fun isSmallGridView() = changeViewListener.viewType.value == ViewType.SMALL_GRID.value

    private fun Int.isListInspirationKeywordItemViewHolder() = this == ListInspirationKeywordItemViewHolder.LAYOUT
    private fun Int.isSmallInspirationKeywordItemViewHolder() = this == SmallInspirationKeywordItemViewHolder.LAYOUT

    private fun Int.isNotEvenNumber() = this % 2 != 0
}
