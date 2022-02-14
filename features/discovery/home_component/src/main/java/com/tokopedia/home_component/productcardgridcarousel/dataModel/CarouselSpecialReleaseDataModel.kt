package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by devarafikry on 08/02/22.
 */
data class CarouselSpecialReleaseDataModel(
    val grid: ChannelGrid,
    val parentPosition: Int,
    val listener: CommonProductCardCarouselListener
) : Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}