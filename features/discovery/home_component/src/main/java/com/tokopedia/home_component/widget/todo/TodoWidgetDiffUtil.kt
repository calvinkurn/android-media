package com.tokopedia.home_component.widget.todo

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel

/**
 * Created by frenzel
 */
internal class TodoWidgetDiffUtil: DiffUtil.ItemCallback<CarouselTodoWidgetDataModel>() {
    override fun areItemsTheSame(
        oldItem: CarouselTodoWidgetDataModel,
        newItem: CarouselTodoWidgetDataModel
    ): Boolean = oldItem.data.id == newItem.data.id

    override fun areContentsTheSame(
        oldItem: CarouselTodoWidgetDataModel,
        newItem: CarouselTodoWidgetDataModel
    ): Boolean = oldItem == newItem
}
