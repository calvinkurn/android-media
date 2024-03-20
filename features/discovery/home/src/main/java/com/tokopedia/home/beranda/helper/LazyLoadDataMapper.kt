package com.tokopedia.home.beranda.helper

import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog
import com.tokopedia.unifycomponents.CardUnify2

/**
 * Created by dhaba
 */
object LazyLoadDataMapper {
    fun mapMissionWidgetData(
        missionWidgetList: List<HomeMissionWidgetData.Mission>,
        isCache: Boolean,
        appLog: HomeMissionWidgetData.AppLog,
    ): List<MissionWidgetDataModel> {
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
                parentProductID = it.parentProductID,
                productName = it.productName,
                recommendationType = it.recommendationType,
                buType = it.buType,
                isTopads = it.isTopads,
                isCarousel = it.isCarousel,
                shopId = it.shopId,
                campaignCode = it.campaignCode,
                animateOnPress = CardUnify2.ANIMATE_OVERLAY_BOUNCE,
                isCache = isCache,
                appLog = RecommendationAppLog(
                    sessionId = appLog.bytedanceSessionId,
                    requestId = appLog.requestId,
                    logId = appLog.logId,
                    recParam = it.recParam
                )
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
