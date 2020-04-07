package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import com.tokopedia.sellerhome.view.model.*
import com.tokopedia.sellerhome.view.viewholder.*

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeAdapterTypeFactory(
        private val sellerHomeFragment: SellerHomeFragment
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

    override fun type(sectionWidget: SectionWidgetUiModel): Int {
        return SectionViewHolder.RES_LAYOUT
    }

    override fun type(progressWidgetWidget: ProgressWidgetUiModel): Int {
        return ProgressViewHolder.RES_LAYOUT
    }

    override fun type(postListWidget: PostListWidgetUiModel): Int {
        return PostListViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            SectionViewHolder.RES_LAYOUT -> SectionViewHolder(parent, sellerHomeFragment)
            CardViewHolder.RES_LAYOUT -> CardViewHolder(parent, sellerHomeFragment)
            LineGraphViewHolder.RES_LAYOUT -> LineGraphViewHolder(parent, sellerHomeFragment)
            CarouselViewHolder.RES_LAYOUT -> CarouselViewHolder(parent, sellerHomeFragment)
            DescriptionViewHolder.RES_LAYOUT -> DescriptionViewHolder(parent)
            ProgressViewHolder.RES_LAYOUT -> ProgressViewHolder(parent, sellerHomeFragment)
            PostListViewHolder.RES_LAYOUT -> PostListViewHolder(parent, sellerHomeFragment)
            else -> super.createViewHolder(parent, type)
        }
    }
}