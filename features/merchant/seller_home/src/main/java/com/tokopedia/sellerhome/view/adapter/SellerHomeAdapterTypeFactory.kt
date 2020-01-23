package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhome.view.model.ProgressUiModel
import com.tokopedia.sellerhome.view.viewholder.CardViewHolder
import com.tokopedia.sellerhome.view.viewholder.LineGraphViewHolder
import com.tokopedia.sellerhome.view.viewholder.ProgressViewHolder

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(cardWidget: CardWidgetUiModel): Int {
        return CardViewHolder.RES_LAYOUT
    }

    fun type(lineGraphWidget: LineGraphWidgetUiModel): Int {
        return LineGraphViewHolder.RES_LAYOUT
    }

    fun type(progressWidget: ProgressUiModel): Int {
        return ProgressViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CardViewHolder.RES_LAYOUT -> CardViewHolder(parent)
            LineGraphViewHolder.RES_LAYOUT -> LineGraphViewHolder(parent)
            ProgressViewHolder.RES_LAYOUT -> ProgressViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}