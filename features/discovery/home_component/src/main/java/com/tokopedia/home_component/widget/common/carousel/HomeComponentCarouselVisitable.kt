package com.tokopedia.home_component.widget.common.carousel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory

interface HomeComponentCarouselVisitable: Visitable<CommonCarouselProductCardTypeFactory> {
    fun getId(): String
    fun equalsWith(visitable: Any?): Boolean
    fun getChangePayloadFrom(visitable: Any?): Bundle? = null
}
