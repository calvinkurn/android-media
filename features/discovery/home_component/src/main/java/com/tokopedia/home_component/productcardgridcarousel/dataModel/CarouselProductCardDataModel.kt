package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig

class CarouselProductCardDataModel (
        val productModel: ProductCardModel,
        val blankSpaceConfig: BlankSpaceConfig = BlankSpaceConfig(),
        val grid: ChannelGrid,
        val trackingAttributionModel: TrackingAttributionModel = TrackingAttributionModel(),
        val impressHolder: ImpressHolder = ImpressHolder(),
        val applink: String = "",
        val componentName: String = "",
        val listener: CommonProductCardCarouselListener? = null,
): Visitable<CommonCarouselProductCardTypeFactory>, HomeComponentCarouselDiffUtil {
    override fun getId(): String {
        return grid.id
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return if(visitable is CarouselProductCardDataModel)
            this == visitable
        else false
    }

    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
