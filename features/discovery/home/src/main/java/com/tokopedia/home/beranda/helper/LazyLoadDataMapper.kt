package com.tokopedia.home.beranda.helper

import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by dhaba
 */
object LazyLoadDataMapper {
    fun mapMissionWidgetData(missionWidgetList: List<HomeMissionWidgetData.Mission>): List<MissionWidgetDataModel> {
        return missionWidgetList.map {
            MissionWidgetDataModel(
                id = it.id,
                title = it.title,
                subTitle = it.subTitle,
                appLink = it.appLink,
                imageURL = it.imageURL,
                pageName = it.pageName,
                categoryID = it.categoryID,
                productID = it.productID,
                productName = it.productName,
                recommendationType = it.recommendationType,
                buType = it.buType,
                isTopads = it.isTopads,
                isCarousel = it.isCarousel,
                shopId = it.shopId,
                animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE,
            )
        }
    }

    fun mapTodoWidgetData(todoWidgetList: List<HomeTodoWidgetData.Todo>): List<TodoWidgetDataModel> {
        return todoWidgetList.map {
            TodoWidgetDataModel(
                id = it.id,
                title = it.title,
                dataSource = it.dataSource,
                dueDate = it.dueDate,
                contextInfo = it.contextInfo,
                price = it.price,
                slashedPrice = it.slashedPrice,
                discountPercentage = it.discountPercentage,
                cardApplink = it.cardApplink,
                ctaType = it.cta.type,
                ctaMode = it.cta.mode,
                ctaText = it.cta.text,
                ctaApplink = it.cta.applink,
                imageUrl = it.imageUrl,
                feParam = it.feParam
            )
        }
    }
}
