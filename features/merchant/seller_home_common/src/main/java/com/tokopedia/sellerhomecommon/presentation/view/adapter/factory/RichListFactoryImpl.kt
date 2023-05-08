package com.tokopedia.sellerhomecommon.presentation.view.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListCaptionViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListRankViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.RichListTickerViewHolder

/**
 * Created by @ilhamsuaib on 17/04/23.
 */

class RichListFactoryImpl : BaseAdapterTypeFactory(), RichListFactory {

    override fun type(model: BaseRichListItemUiModel.RankItemUiModel): Int {
        return RichListRankViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseRichListItemUiModel.CaptionItemUiModel): Int {
        return RichListCaptionViewHolder.RES_LAYOUT
    }

    override fun type(model: BaseRichListItemUiModel.TickerItemUiModel): Int {
        return RichListTickerViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            RichListRankViewHolder.RES_LAYOUT -> RichListRankViewHolder(parent)
            RichListCaptionViewHolder.RES_LAYOUT -> RichListCaptionViewHolder(parent)
            RichListTickerViewHolder.RES_LAYOUT -> RichListTickerViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}