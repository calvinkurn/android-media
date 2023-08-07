package com.tokopedia.home_component.widget.todo

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.productcardgridcarousel.viewHolder.TodoWidgetItemViewHolder

class TodoWidgetTypeFactoryImpl : CommonCarouselProductCardTypeFactoryImpl(), TodoWidgetTypeFactory {
    override fun type(dataModel: CarouselTodoWidgetDataModel): Int {
        return TodoWidgetItemViewHolder.LAYOUT
    }

    override fun type(dataModel: TodoErrorDataModel): Int {
        return TodoErrorViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            TodoWidgetItemViewHolder.LAYOUT -> TodoWidgetItemViewHolder(view)
            TodoErrorViewHolder.LAYOUT -> TodoErrorViewHolder(view)
            else -> super.createViewHolder(view, viewType)
        }
    }
}
