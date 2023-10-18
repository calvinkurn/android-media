package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.view.adapter.RichListAdapter
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListCaptionViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListErrorStateViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListLoadingStateViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListRankViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListTickerViewHolder

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

class RichListFactoryImpl(
    private val listener: RichListAdapter.Listener
) : BaseAdapterTypeFactory(), RichListFactory {

    override fun type(model: BaseRichListItem.RankItemUiModel): Int {
        return RichListRankViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseRichListItem.CaptionItemUiModel): Int {
        return RichListCaptionViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseRichListItem.TickerItemUiModel): Int {
        return RichListTickerViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseRichListItem.LoadingStateUiModel): Int {
        return RichListLoadingStateViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseRichListItem.ErrorStateUiModel): Int {
        return RichListErrorStateViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RichListRankViewHolder.RES_LAYOUT -> RichListRankViewHolder(parent) { tooltip ->
                listener.setOnTooltipClicked(tooltip)
            }
            RichListCaptionViewHolder.RES_LAYOUT -> RichListCaptionViewHolder(parent) {
                listener.setOnCtaClicked(it)
            }
            RichListTickerViewHolder.RES_LAYOUT -> RichListTickerViewHolder(parent) {
                listener.setOnCtaClicked(it)
            }
            RichListLoadingStateViewHolder.RES_LAYOUT -> RichListLoadingStateViewHolder(parent)
            RichListErrorStateViewHolder.RES_LAYOUT -> RichListErrorStateViewHolder(parent) {
                listener.setOnReloadClicked()
            }
            else -> super.createViewHolder(parent, type)
        }
    }
}