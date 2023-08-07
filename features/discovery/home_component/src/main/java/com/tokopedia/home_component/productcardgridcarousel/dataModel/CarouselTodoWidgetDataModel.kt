package com.tokopedia.home_component.productcardgridcarousel.dataModel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.viewholders.TodoWidgetDismissListener
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
data class CarouselTodoWidgetDataModel(
    val data: TodoWidgetDataModel,
    val channelId: String,
    val headerName: String,
    val cardInteraction: Boolean,
    val isCarousel: Boolean,
    val verticalPosition: Int,
    val cardPosition: Int,
    val todoWidgetComponentListener: TodoWidgetComponentListener,
    val todoWidgetDismissListener: TodoWidgetDismissListener,
) : Visitable<CommonCarouselProductCardTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: CommonCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
