package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.TodoWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.util.TodoWidgetUtil.parseCloseParam
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
        if (element.cardApplink.isNotBlank()) {
            homeCategoryListener.onDynamicChannelClicked(element.cardApplink)
        }
    }

    override fun onTodoCTAClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int) {
        TodoWidgetTracking.sendTodoWidgetCTAClicked(element, horizontalPosition, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(element.ctaApplink)
    }

    override fun onTodoCloseClicked(element: CarouselTodoWidgetDataModel, horizontalPosition: Int, isLastItem: Boolean) {
        TodoWidgetTracking.sendTodoWidgetCloseClicked(element)
        homeRevampViewModel.dismissTodoWidget(horizontalPosition, element.dataSource, element.feParam.parseCloseParam(), isLastItem)
    }

    override fun onTodoImpressed(element: CarouselTodoWidgetDataModel, horizontalPosition: Int) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
            TodoWidgetTracking.getTodoWidgetView(
                element,
                horizontalPosition,
                homeCategoryListener.userId
            ) as HashMap<String, Any>
        )
    }

    override fun refreshTodowidget(element: TodoWidgetListDataModel) {
        homeRevampViewModel.getTodoWidgetRefresh()
    }
}
