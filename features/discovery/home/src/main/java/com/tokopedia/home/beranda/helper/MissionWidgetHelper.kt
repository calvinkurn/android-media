package com.tokopedia.home.beranda.helper

import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
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
                        imageURL = pojo.imageURL,
                        pageName = pojo.pageName,
                        categoryID = pojo.categoryID,
                        productID = pojo.productID,
                        productName = pojo.productName,
                        recommendationType = pojo.recommendationType,
                        buType = pojo.buType,
                        isTopads = pojo.isTopads,
                        isCarousel = pojo.isCarousel
                    )
            )
        }
        return dataList
    }
}