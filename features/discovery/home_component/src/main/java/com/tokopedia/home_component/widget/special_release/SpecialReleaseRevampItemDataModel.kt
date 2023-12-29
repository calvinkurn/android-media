package com.tokopedia.home_component.widget.special_release

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil
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
): Visitable<SpecialReleaseRevampItemTypeFactory>, HomeComponentCarouselDiffUtil {
    override fun type(typeFactory: SpecialReleaseRevampItemTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getId(): String {
        return grid.id
    }

    override fun equalsWith(visitable: Any?): Boolean {
        return if(visitable is SpecialReleaseRevampItemDataModel) {
            visitable == this
        } else false
    }
}
