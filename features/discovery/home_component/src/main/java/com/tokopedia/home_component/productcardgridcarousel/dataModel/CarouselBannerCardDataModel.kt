package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.BannerMixLeftPaddingListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

/**
 * Created by dhaba
 */
class CarouselBannerCardDataModel(
    val channel: ChannelModel,
    val listener: BannerMixLeftPaddingListener,
) : Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
