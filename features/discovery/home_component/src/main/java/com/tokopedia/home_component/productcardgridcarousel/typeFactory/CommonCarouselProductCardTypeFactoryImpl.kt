package com.tokopedia.home_component.productcardgridcarousel.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.*
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.*

/**
 * @author by yoasfs on 09/06/20
 */

class CommonCarouselProductCardTypeFactoryImpl(
    private val channels: ChannelModel,
    private val cardInteraction: Boolean = false
) : BaseAdapterTypeFactory(), CommonCarouselProductCardTypeFactory {

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

    override fun type(dataModel: CarouselViewAllCardDataModel): Int {
        return CarouselViewAllCardViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselCampaignCardDataModel): Int {
        return CarouselCampaignCardViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselMerchantVoucherDataModel): Int {
        return CarouselMerchantVoucherViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselSpecialReleaseDataModel): Int {
        return SpecialReleaseItemViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselMissionWidgetDataModel): Int {
        return MissionWidgetItemViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselBannerCardDataModel): Int {
        return CarouselBannerItemViewHolder.LAYOUT
    }

    override fun type(dataModel: CarouselTodoWidgetDataModel): Int {
        return TodoWidgetItemViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CarouselProductCardViewHolder.LAYOUT -> {
                CarouselProductCardViewHolder(parent, channels)
            }
            CarouselSeeMorePdpViewHolder.LAYOUT -> {
                CarouselSeeMorePdpViewHolder(parent, channels, cardInteraction)
            }
            CarouselEmptyCardViewHolder.LAYOUT -> {
                CarouselEmptyCardViewHolder(parent)
            }
            CarouselFeaturedShopViewHolder.LAYOUT -> {
                CarouselFeaturedShopViewHolder(parent, channels, cardInteraction)
            }
            CarouselViewAllCardViewHolder.LAYOUT -> {
                CarouselViewAllCardViewHolder(parent, channels, cardInteraction)
            }
            CarouselCampaignCardViewHolder.LAYOUT -> {
                CarouselCampaignCardViewHolder(parent, channels, cardInteraction)
            }
            CarouselMerchantVoucherViewHolder.LAYOUT -> {
                CarouselMerchantVoucherViewHolder(parent, cardInteraction)
            }
            SpecialReleaseItemViewHolder.LAYOUT -> {
                SpecialReleaseItemViewHolder(parent, channels, cardInteraction)
            }
            MissionWidgetItemViewHolder.LAYOUT -> {
                MissionWidgetItemViewHolder(parent, cardInteraction)
            }
            CarouselBannerItemViewHolder.LAYOUT -> {
                CarouselBannerItemViewHolder(parent, cardInteraction)
            }
            TodoWidgetItemViewHolder.LAYOUT -> {
                TodoWidgetItemViewHolder(parent)
            }
            else -> {
                super.createViewHolder(parent, type)
            }
        }
    }
}
