package com.tokopedia.home_component.productcardgridcarousel.typeFactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselEmptyCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselFeaturedShopCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselSeeMorePdpDataModel

interface CommonCarouselProductCardTypeFactory: AdapterTypeFactory {
    fun type(cardDataModelCarousel: CarouselEmptyCardDataModel) : Int
    fun type(dataModel: CarouselProductCardDataModel) : Int
    fun type(dataModelCarousel: CarouselSeeMorePdpDataModel) : Int
    fun type(dataModel: CarouselFeaturedShopCardDataModel) : Int

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>
}
