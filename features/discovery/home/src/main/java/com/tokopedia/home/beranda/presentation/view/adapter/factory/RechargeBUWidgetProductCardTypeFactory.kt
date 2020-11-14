package com.tokopedia.home.beranda.presentation.view.adapter.factory

import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargeBUWidgetProductCardModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

interface RechargeBUWidgetProductCardTypeFactory: CommonCarouselProductCardTypeFactory {
    fun type(rechargeBUWidgetProductCardModel: RechargeBUWidgetProductCardModel) : Int
}