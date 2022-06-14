package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MissionWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.track.TrackApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Created by dhaba
 */
@ExperimentalCoroutinesApi
@FlowPreview
class MissionWidgetComponentCallback(
    val homeCategoryListener: HomeCategoryListener,
    val homeRevampViewModel: HomeRevampViewModel
) :
    MissionWidgetComponentListener {
    override fun refreshMissionWidget(missionWidgetListDataModel: MissionWidgetListDataModel) {
        homeRevampViewModel.getMissionWidgetRefresh()
    }

    override fun onMissionClicked(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int
    ) {
        MissionWidgetTracking.getMissionWidgetClicked(element, horizontalPosition, homeCategoryListener.userId)
        homeCategoryListener.onDynamicChannelClicked(element.appLink)
    }

    override fun onMissionImpressed(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int
    ) {

    }
}