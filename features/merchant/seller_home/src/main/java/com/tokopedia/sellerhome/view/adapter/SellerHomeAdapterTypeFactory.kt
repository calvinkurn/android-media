package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.BaseWidgetUiModel
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import com.tokopedia.sellerhome.view.model.DescriptionWidgetUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhome.view.model.SectionWidgetUiModel
import com.tokopedia.sellerhome.view.viewholder.CardViewHolder
import com.tokopedia.sellerhome.view.viewholder.DescriptionViewHolder
import com.tokopedia.sellerhome.view.viewholder.LineGraphViewHolder
import com.tokopedia.sellerhome.view.viewholder.SectionViewHolder

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

    fun type(descriptionWidget: DescriptionWidgetUiModel): Int {
        return DescriptionViewHolder.RES_LAYOUT
    }
      
    fun type(sectionWdget: SectionWidgetUiModel): Int {
        return SectionViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionViewHolder.RES_LAYOUT -> SectionViewHolder(parent)
            CardViewHolder.RES_LAYOUT -> CardViewHolder(parent)
            LineGraphViewHolder.RES_LAYOUT -> LineGraphViewHolder(parent)
            DescriptionViewHolder.RES_LAYOUT -> DescriptionViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}