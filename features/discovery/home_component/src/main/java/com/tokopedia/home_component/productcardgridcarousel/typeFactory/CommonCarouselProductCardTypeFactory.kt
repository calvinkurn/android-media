package com.tokopedia.home_component.productcardgridcarousel.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.*
import com.tokopedia.home_component.widget.special_release.SpecialReleaseRevampItemDataModel

interface CommonCarouselProductCardTypeFactory: AdapterTypeFactory {
    fun type(cardDataModelCarousel: CarouselEmptyCardDataModel): Int = 0
    fun type(dataModel: CarouselProductCardDataModel): Int = 0
    fun type(dataModelCarousel: CarouselSeeMorePdpDataModel): Int = 0
    fun type(dataModel: CarouselFeaturedShopCardDataModel): Int = 0
    fun type(dataModel: CarouselViewAllCardDataModel): Int = 0
    fun type(dataModel: CarouselCampaignCardDataModel): Int = 0
    fun type(dataModel: CarouselMerchantVoucherDataModel): Int = 0
    fun type(dataModel: CarouselSpecialReleaseDataModel): Int = 0
    fun type(dataModel: CarouselBannerCardDataModel): Int = 0
    fun type(dataModel: SpecialReleaseRevampItemDataModel): Int = 0

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
