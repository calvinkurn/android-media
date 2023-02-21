package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Created by frenzel
 */
@ExperimentalCoroutinesApi
@FlowPreview
class TodoWidgetComponentCallback(
    val homeCategoryListener: HomeCategoryListener,
    val homeRevampViewModel: HomeRevampViewModel
) : TodoWidgetComponentListener {

    override fun onTodoCardClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int) {
    }

    override fun onTodoCTAClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int) {
    }

    override fun onTodoCloseClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int) {
    }

    override fun onTodoImpressed(element: CarouselTodoWidgetDataModel, horizontalPosition: Int) {
    }

    override fun refreshTodowidget(element: TodoWidgetListDataModel) {
        homeRevampViewModel.getTodoWidgetRefresh()
    }
}
