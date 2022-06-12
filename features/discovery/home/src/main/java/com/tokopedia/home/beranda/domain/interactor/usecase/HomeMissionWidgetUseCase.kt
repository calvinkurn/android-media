package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.beranda.helper.MissionWidgetHelper
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
class HomeMissionWidgetUseCase @Inject constructor(
        private val missionWidgetRepository: HomeMissionWidgetRepository
        ) {

    suspend fun onMissionWidgetRefresh(currentMissionWidgetListDataModel: MissionWidgetListDataModel): MissionWidgetListDataModel {
        return try {
            val results = missionWidgetRepository.executeOnBackground()
            val resultList = MissionWidgetHelper.convertMissionWidgetDataList(results.getHomeMissionWidget.missions)
            currentMissionWidgetListDataModel.copy(
                missionWidgetList = resultList,
                status = ConstantKey.LoadDataKey.STATUS_SUCCESS
            )
        } catch (e: Exception) {
            currentMissionWidgetListDataModel.copy(status = ConstantKey.LoadDataKey.STATUS_ERROR)
        }
    }
}