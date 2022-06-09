package com.tokopedia.home_component.visitable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by dhaba
 */
data class MissionWidgetDataModel(
        val id: Long = 0L,
        val title: String = "",
        val subTitle: String = "",
        val appLink: String = "",
        val url: String = "",
        val imageURL: String = "",
        val impressHolder: ImpressHolder = ImpressHolder()
): Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}