package com.tokopedia.home_component.productcardgridcarousel.dataModel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil
import com.tokopedia.home_component.widget.todo.TodoWidgetTypeFactory
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
) : Visitable<TodoWidgetTypeFactory>, HomeComponentCarouselDiffUtil, ImpressHolder() {

    companion object {
        const val PAYLOAD_ITEM_POSITION = "item_position"
    }

    override fun type(typeFactory: TodoWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getId(): String = data.id.toString()

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is CarouselTodoWidgetDataModel && visitable == this
    }

    override fun getChangePayloadFrom(visitable: Any?): Bundle {
        val bundle = Bundle()
        if(visitable is CarouselTodoWidgetDataModel && visitable.cardPosition != cardPosition) {
            bundle.putBoolean(PAYLOAD_ITEM_POSITION, true)
        }
        return bundle
    }
}
