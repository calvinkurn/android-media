package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.SellerHomeWidgetTooltipClickListener
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.viewholder.*

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeAdapterTypeFactory(
        private val tooltipClickListener: SellerHomeWidgetTooltipClickListener,
        private val cardWidgetListener: CardViewHolder.Listener,
        private val lineGraphWidgetListener: LineGraphViewHolder.Listener
) : BaseAdapterTypeFactory(), SellerHomeTypeFactory {

    override fun type(cardWidget: CardWidgetUiModel): Int {
        return CardViewHolder.RES_LAYOUT
    }

    override fun type(lineGraphWidget: LineGraphWidgetUiModel): Int {
        return LineGraphViewHolder.RES_LAYOUT
    }

    override fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int {
        return CarouselViewHolder.RES_LAYOUT
    }

    override fun type(descriptionWidget: DescriptionWidgetUiModel): Int {
        return DescriptionViewHolder.RES_LAYOUT
    }

    override fun type(sectionWdget: SectionWidgetUiModel): Int {
        return SectionViewHolder.RES_LAYOUT
    }

    override fun type(progressWidgetWidget: ProgressWidgetUiModel): Int {
        return ProgressViewHolder.RES_LAYOUT
    }

    override fun type(listWidget: ListWidgetUiModel): Int {
        return ListViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionViewHolder.RES_LAYOUT -> SectionViewHolder(parent)
            CardViewHolder.RES_LAYOUT -> CardViewHolder(parent, cardWidgetListener)
            LineGraphViewHolder.RES_LAYOUT -> LineGraphViewHolder(parent, lineGraphWidgetListener)
            CarouselViewHolder.RES_LAYOUT -> CarouselViewHolder(parent)
            DescriptionViewHolder.RES_LAYOUT -> DescriptionViewHolder(parent)
            ProgressViewHolder.RES_LAYOUT -> ProgressViewHolder(parent, tooltipClickListener)
            ListViewHolder.RES_LAYOUT -> ListViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}