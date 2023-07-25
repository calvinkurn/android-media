package com.tokopedia.home_component.widget.special_release

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
data class SpecialReleaseRevampItemDataModel (
    val grid: ChannelGrid,
    val channel: ChannelModel,
    val shopImpressHolder: ImpressHolder = ImpressHolder(),
    val productImpressHolder: ImpressHolder = ImpressHolder(),
    val listener: SpecialReleaseRevampListener
): Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
