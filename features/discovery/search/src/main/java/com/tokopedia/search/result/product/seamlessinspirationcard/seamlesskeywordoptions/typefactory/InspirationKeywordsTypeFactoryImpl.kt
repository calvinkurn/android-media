package com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.result.product.changeview.ChangeViewListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordListener
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.ListInspirationKeywordItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.NoImageInspirationKeywordItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.viewholder.SmallInspirationKeywordItemViewHolder
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isBigGridView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isListView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.isSmallGridView

class InspirationKeywordsTypeFactoryImpl(
    private val inspirationKeywordListener: InspirationKeywordListener,
    private val changeViewListener: ChangeViewListener,
    private val columnQty: Int,
    private val isNoImageCard: Boolean
) :
    BaseAdapterTypeFactory(), InspirationKeywordsTypeFactory {
    override fun type(item: InspirationKeywordDataView): Int {
        return when {
            isNoImageCard -> NoImageInspirationKeywordItemViewHolder.LAYOUT
            isListView() || isBigGridView() ->
                ListInspirationKeywordItemViewHolder.LAYOUT
            isSmallGridView() ->
                SmallInspirationKeywordItemViewHolder.LAYOUT
            else -> throw TypeNotSupportedException.create("Layout not supported")
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
            isNoImageCard -> NoImageInspirationKeywordItemViewHolder(
                view,
                inspirationKeywordListener,
                changeViewListener
            )
            else -> super.createViewHolder(view, type)
        }
    }

    private fun isListView() = changeViewListener.isListView()
    private fun isBigGridView() = changeViewListener.isBigGridView()
    private fun isSmallGridView() = changeViewListener.isSmallGridView()

    private fun Int.isListInspirationKeywordItemViewHolder() = this == ListInspirationKeywordItemViewHolder.LAYOUT
    private fun Int.isSmallInspirationKeywordItemViewHolder() = this == SmallInspirationKeywordItemViewHolder.LAYOUT

    private fun Int.isNotEvenNumber() = this % 2 != 0
}
