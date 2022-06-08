package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.interactor.repository.HomeMissionWidgetRepository
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
            val resultList = convertMissionWidgetDataList(results.missionWidget.missions)
            currentMissionWidgetListDataModel.copy(
                missionWidgetList = resultList,
                isErrorLoad = false
            )
        } catch (e: Exception) {
            currentMissionWidgetListDataModel.copy(isErrorLoad = true)
        }
    }

    private fun convertMissionWidgetDataList(missionWidgetList: List<HomeWidget.Mission>): MutableList<MissionWidgetDataModel> {
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