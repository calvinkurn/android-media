package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.data.model.HomeMissionWidgetData
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import javax.inject.Inject

/**
 * Created by dhaba
 */
class HomeMissionWidgetUseCase @Inject constructor(
        private val missionWidgetRepository: HomeMissionWidgetRepository
        ) {

    suspend fun onMissionWidgetRefresh(refreshCount: Int, currentMissionWidgetListDataModel: MissionWidgetListDataModel): MissionWidgetListDataModel {
        return try {
            val results = missionWidgetRepository.executeOnBackground()
            val resultList = convertMissionWidgetDataList(results.getHomeMissionWidget.missions)
            currentMissionWidgetListDataModel.copy(
                missionWidgetList = resultList,
                status = ConstantKey.LoadDataKey.STATUS_SUCCESS
            )
        } catch (e: Exception) {
            currentMissionWidgetListDataModel.copy(status = ConstantKey.LoadDataKey.STATUS_ERROR)
        }
    }

    private fun convertMissionWidgetDataList(missionWidgetList: List<HomeMissionWidgetData.Mission>): MutableList<MissionWidgetDataModel> {
        val dataList: MutableList<MissionWidgetDataModel> = mutableListOf()
        for (pojo in missionWidgetList) {
            dataList.add(
                    MissionWidgetDataModel(
                        id = pojo.id,
                        title = pojo.title,
                        subTitle = pojo.subTitle,
                        appLink = pojo.appLink,
                        url = pojo.url,
                        imageURL = pojo.imageURL
                    )
            )
        }
        return dataList
    }
}