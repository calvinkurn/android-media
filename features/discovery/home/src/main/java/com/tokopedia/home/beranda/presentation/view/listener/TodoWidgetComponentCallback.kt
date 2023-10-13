package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.TodoWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Created by frenzel
 */
@ExperimentalCoroutinesApi
@FlowPreview
class TodoWidgetComponentCallback(
    val homeCategoryListener: HomeCategoryListener,
    private val homeRevampViewModel: HomeRevampViewModel
) : TodoWidgetComponentListener {

    override fun onTodoCardClicked(element: CarouselTodoWidgetDataModel) {
        TodoWidgetTracking.sendTodoWidgetCardClicked(element)
        if (element.data.cardApplink.isNotBlank()) {
            homeCategoryListener.onDynamicChannelClicked(element.data.cardApplink)
        }
    }

    override fun onTodoCTAClicked(element: CarouselTodoWidgetDataModel) {
        TodoWidgetTracking.sendTodoWidgetCTAClicked(element, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(element.data.ctaApplink)
    }

    override fun onTodoCloseClicked(element: CarouselTodoWidgetDataModel) {
        TodoWidgetTracking.sendTodoWidgetCloseClicked(element)
        homeRevampViewModel.dismissTodoWidget(element.cardPosition, element.data.dataSource, element.data.feParam)
    }

    override fun onTodoImpressed(element: CarouselTodoWidgetDataModel) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
            TodoWidgetTracking.getTodoWidgetView(
                element,
                homeCategoryListener.userId
            ) as HashMap<String, Any>
        )
    }

    override fun refreshTodowidget() {
        homeRevampViewModel.getTodoWidgetRefresh()
    }
}
