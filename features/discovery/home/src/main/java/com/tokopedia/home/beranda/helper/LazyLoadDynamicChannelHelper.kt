package com.tokopedia.home.beranda.helper

import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetDataModel

/**
 * Created by dhaba
 */
object LazyLoadDynamicChannelHelper {
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
                    isCarousel = pojo.isCarousel,
                    shopId = pojo.shopId
                )
            )
        }
        return dataList
    }

    fun convertTodoWidgetDataList(todoWidgetList: List<HomeTodoWidgetData.Todo>): MutableList<TodoWidgetDataModel> {
        val dataList: MutableList<TodoWidgetDataModel> = mutableListOf()
        for (pojo in todoWidgetList) {
            dataList.add(
                TodoWidgetDataModel(
                    id = pojo.id,
                    title = pojo.title,
                    dataSource = pojo.dataSource,
                    dueDate = pojo.dueDate,
                    contextInfo = pojo.contextInfo,
                    price = pojo.price,
                    slashedPrice = pojo.slashedPrice,
                    discountPercentage = pojo.discountPercentage,
                    cardApplink = pojo.cardApplink,
                    ctaType = pojo.cta.type,
                    ctaMode = pojo.cta.mode,
                    ctaText = pojo.cta.text,
                    ctaApplink = pojo.cta.applink,
                    imageUrl = pojo.imageUrl,
                    feParam = pojo.feParam
                )
            )
        }
        return dataList
    }
}
