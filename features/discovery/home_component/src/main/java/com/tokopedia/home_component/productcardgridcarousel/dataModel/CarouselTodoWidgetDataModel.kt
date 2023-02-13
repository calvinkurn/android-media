package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
class CarouselTodoWidgetDataModel(
    val id: Long = 0L,
    val title: String = "",
    val dueDate: String = "",
    val description: String = "",
    val price: String = "",
    val slashedPrice: String = "",
    val cardApplink: String = "",
    val ctaApplink: String = "",
    val imageUrl: String = "",
    val recommendationType: String = "",
    val buType: String = "",
    val todoWidgetComponentListener: TodoWidgetComponentListener,
    val channel: ChannelModel,
    val verticalPosition: Int = 0,
    val cardInteraction: Boolean = false,
) : Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
