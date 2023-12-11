package com.tokopedia.home_component.widget.special_release

import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

interface SpecialReleaseRevampItemTypeFactory: CommonCarouselProductCardTypeFactory {
    fun type(dataModel: SpecialReleaseRevampItemDataModel) = 0
}
