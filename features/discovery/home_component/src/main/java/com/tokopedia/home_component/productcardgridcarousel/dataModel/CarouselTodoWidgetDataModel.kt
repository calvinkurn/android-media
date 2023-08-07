package com.tokopedia.home_component.productcardgridcarousel.dataModel

import android.os.Bundle
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.viewholders.TodoWidgetDismissListener
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.home_component.widget.todo.TodoWidgetVisitable
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
    val todoWidgetComponentListener: TodoWidgetComponentListener,
    val todoWidgetDismissListener: TodoWidgetDismissListener,
) : TodoWidgetVisitable, ImpressHolder() {

    companion object {
        const val PAYLOAD_ITEM_POSITION = "item_position"
    }

    override fun getId(): String = data.id.toString()

    override fun equalsWith(visitable: TodoWidgetVisitable): Boolean {
        return visitable is CarouselTodoWidgetDataModel && visitable == this
    }

    override fun getChangePayloadFrom(visitable: TodoWidgetVisitable): Bundle {
        val bundle = Bundle()
        if(visitable is CarouselTodoWidgetDataModel && visitable.cardPosition != cardPosition) {
            bundle.putBoolean(PAYLOAD_ITEM_POSITION, true)
        }
        return bundle
    }

    override fun type(typeFactory: TodoWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
