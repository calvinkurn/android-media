package com.tokopedia.home_component.productcardgridcarousel.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.*

interface CommonCarouselProductCardTypeFactory: AdapterTypeFactory {
    fun type(cardDataModelCarousel: CarouselEmptyCardDataModel): Int
    fun type(dataModel: CarouselProductCardDataModel): Int
    fun type(dataModelCarousel: CarouselSeeMorePdpDataModel): Int
    fun type(dataModel: CarouselFeaturedShopCardDataModel): Int
    fun type(dataModel: CarouselViewAllCardDataModel): Int = 0
    fun type(dataModel: CarouselCampaignCardDataModel): Int = 0
    fun type(dataModel: CarouselSpecialReleaseDataModel): Int = 0

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
