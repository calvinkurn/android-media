package com.tokopedia.home_component.listener

import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel

/**
 * Created by frenzel
 */
interface TodoWidgetComponentListener {
    fun onTodoCardClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int)
    fun onTodoCTAClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int)
    fun onTodoCloseClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int)
    fun onTodoImpressed(element: CarouselTodoWidgetDataModel, horizontalPosition: Int)
    fun refreshTodowidget(element: TodoWidgetListDataModel)
}
