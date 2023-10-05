package com.tokopedia.home_component.widget.special_release

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel

/**
 * Created by frenzel
 */
data class SpecialReleaseRevampItemDataModel (
    val grid: ChannelGrid,
    val productCardModel: ProductCardModel,
    val trackingAttributionModel: TrackingAttributionModel,
    val cardInteraction: Int,
    val shopImpressHolder: ImpressHolder = ImpressHolder(),
    val productImpressHolder: ImpressHolder = ImpressHolder(),
    val listener: SpecialReleaseRevampListener
): Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
