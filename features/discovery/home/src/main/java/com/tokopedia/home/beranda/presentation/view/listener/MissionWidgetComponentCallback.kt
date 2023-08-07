package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MissionWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
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
    companion object {
        private const val ZERO_PRODUCT_ID = "0"
    }

    override fun refreshMissionWidget(missionWidgetListDataModel: MissionWidgetListDataModel) {
        homeRevampViewModel.getMissionWidgetRefresh()
    }

    override fun onMissionClicked(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int
    ) {
        if (element.isProduct()) {
            MissionWidgetTracking.sendMissionWidgetClickedToPdp(element, horizontalPosition, homeCategoryListener.userId)
        } else {
            MissionWidgetTracking.sendMissionWidgetClicked(element, horizontalPosition, homeCategoryListener.userId)
        }
        homeCategoryListener.onDynamicChannelClicked(element.data.appLink)
    }

    override fun onMissionImpressed(
        element: CarouselMissionWidgetDataModel,
        horizontalPosition: Int
    ) {
        if (element.isProduct()) {
            homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MissionWidgetTracking.getMissionWidgetProductView(
                    element,
                    horizontalPosition,
                    homeCategoryListener.userId
                ) as HashMap<String, Any>
            )
        } else {
            homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MissionWidgetTracking.getMissionWidgetView(
                    element,
                    horizontalPosition,
                    homeCategoryListener.userId
                ) as HashMap<String, Any>
            )
        }
    }

    private fun CarouselMissionWidgetDataModel.isProduct(): Boolean {
        return this.data.productID.isNotBlank() && this.data.productID != ZERO_PRODUCT_ID
    }
}
