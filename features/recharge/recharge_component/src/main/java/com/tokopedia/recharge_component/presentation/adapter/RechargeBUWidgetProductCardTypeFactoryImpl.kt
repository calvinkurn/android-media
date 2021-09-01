package com.tokopedia.recharge_component.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.*
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.*
import com.tokopedia.recharge_component.model.RechargeBUWidgetProductCardModel
import com.tokopedia.recharge_component.presentation.adapter.viewholder.RechargeBUWidgetProductCardViewHolder

/**
 * @author by resakemal on 12/11/20
 */

class RechargeBUWidgetProductCardTypeFactoryImpl(private val channels: ChannelModel) :
        BaseAdapterTypeFactory(), RechargeBUWidgetProductCardTypeFactory {

    override fun type(cardDataModelCarousel: CarouselEmptyCardDataModel): Int {
        return CarouselEmptyCardViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselProductCardDataModel): Int {
        return CarouselProductCardViewHolder.LAYOUT
    }

    override fun type(dataModelCarousel: CarouselSeeMorePdpDataModel): Int {
        return CarouselSeeMorePdpViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselFeaturedShopCardDataModel): Int {
        return CarouselFeaturedShopViewHolder.LAYOUT
    }

    override fun type(rechargeBUWidgetProductCardModel: RechargeBUWidgetProductCardModel): Int {
        return RechargeBUWidgetProductCardViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselViewAllCardDataModel): Int {
        return CarouselViewAllCardViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CarouselProductCardViewHolder.LAYOUT -> {
                CarouselProductCardViewHolder(parent, channels)
            }
            CarouselSeeMorePdpViewHolder.LAYOUT -> {
                CarouselSeeMorePdpViewHolder(parent, channels)
            }
            CarouselEmptyCardViewHolder.LAYOUT -> {
                CarouselEmptyCardViewHolder(parent)
            }
            CarouselFeaturedShopViewHolder.LAYOUT -> {
                CarouselFeaturedShopViewHolder(parent, channels)
            }
            RechargeBUWidgetProductCardViewHolder.LAYOUT -> {
                RechargeBUWidgetProductCardViewHolder(parent, channels)
            }
            CarouselViewAllCardViewHolder.LAYOUT -> {
                CarouselViewAllCardViewHolder(parent, channels)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}