package com.tokopedia.home_component.listener

import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel

/**
 * Created by frenzel
 */
interface TodoWidgetComponentListener {
    fun onTodoCardClicked(element: CarouselTodoWidgetDataModel)
    fun onTodoCTAClicked(element: CarouselTodoWidgetDataModel)
    fun onTodoCloseClicked(element: CarouselTodoWidgetDataModel)
    fun onTodoImpressed(element: CarouselTodoWidgetDataModel)
    fun refreshTodowidget()
}
