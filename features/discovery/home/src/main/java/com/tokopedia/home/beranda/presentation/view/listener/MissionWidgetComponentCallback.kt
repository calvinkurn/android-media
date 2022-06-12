package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.listener.MissionWidgetComponentListener
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
    override fun refreshMissionWidget(missionWidgetListDataModel: MissionWidgetListDataModel) {
        homeRevampViewModel.getMissionWidgetRefresh()
    }
}