package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

/**
 * Created by dhaba
 */
class CarouselBannerCardDataModel(
    val imageUrl: String,
    val horizontalPosition: Int,
    val gradientColor: ArrayList<String> = arrayListOf(""),
    val listener: CommonProductCardCarouselListener
) : Visitable<CommonCarouselProductCardTypeFactory> {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
