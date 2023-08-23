package com.tokopedia.home_component.productcardgridcarousel.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.*
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.*
import com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampItemDataModel
import com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampItemViewHolder

/**
 * @author by yoasfs on 09/06/20
 */

open class CommonCarouselProductCardTypeFactoryImpl(
    @Deprecated("Please ignore passing this field to avoid re-instantiating everytime data changes. Only pass the necessary data on the respective models instead.")
    private val channels: ChannelModel = ChannelModel(id = "0", groupId = "0"),
    private val cardInteraction: Boolean = false,
    private val listener: CommonProductCardCarouselListener? = null,
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

    override fun type(dataModel: SpecialReleaseRevampItemDataModel): Int {
        return SpecialReleaseRevampItemViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            CarouselProductCardViewHolder.LAYOUT -> {
                CarouselProductCardViewHolder(view, channels, listener)
            }
            CarouselSeeMorePdpViewHolder.LAYOUT -> {
                CarouselSeeMorePdpViewHolder(view, channels, cardInteraction)
            }
            CarouselEmptyCardViewHolder.LAYOUT -> {
                CarouselEmptyCardViewHolder(view)
            }
            CarouselFeaturedShopViewHolder.LAYOUT -> {
                CarouselFeaturedShopViewHolder(view, channels, cardInteraction)
            }
            CarouselViewAllCardViewHolder.LAYOUT -> {
                CarouselViewAllCardViewHolder(view, channels, cardInteraction, listener)
            }
            CarouselCampaignCardViewHolder.LAYOUT -> {
                CarouselCampaignCardViewHolder(view, channels, cardInteraction)
            }
            CarouselMerchantVoucherViewHolder.LAYOUT -> {
                CarouselMerchantVoucherViewHolder(view, cardInteraction)
            }
            SpecialReleaseItemViewHolder.LAYOUT -> {
                SpecialReleaseItemViewHolder(view, channels, cardInteraction)
            }
            MissionWidgetItemViewHolder.LAYOUT -> {
                MissionWidgetItemViewHolder(view, cardInteraction)
            }
            CarouselBannerItemViewHolder.LAYOUT -> {
                CarouselBannerItemViewHolder(view, cardInteraction)
            }
            TodoWidgetItemViewHolder.LAYOUT -> {
                TodoWidgetItemViewHolder(view)
            }
            SpecialReleaseRevampItemViewHolder.LAYOUT -> {
                SpecialReleaseRevampItemViewHolder(view)
            }
            else -> {
                super.createViewHolder(view, viewType)
            }
        }
    }
}
