package com.tokopedia.home.beranda.helper

import com.tokopedia.home.beranda.data.model.HomeMissionWidgetData
import com.tokopedia.home_component.visitable.MissionWidgetDataModel

/**
 * Created by dhaba
 */
object MissionWidgetHelper {
    fun convertMissionWidgetDataList(missionWidgetList: List<HomeMissionWidgetData.Mission>): MutableList<MissionWidgetDataModel> {
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