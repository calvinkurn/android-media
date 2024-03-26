package com.tokopedia.home.beranda.helper

import com.tokopedia.home_component.mapper.Mission4SquareWidgetMapper
import com.tokopedia.home_component.usecase.missionwidget.HomeMissionWidgetData
import com.tokopedia.home_component.usecase.todowidget.HomeTodoWidgetData
import com.tokopedia.home_component.visitable.Mission4SquareUiModel
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
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
                ),
                labelGroup = it.labelGroup.map { labelGroup ->
                    MissionWidgetDataModel.LabelGroup(
                        title = labelGroup.title,
                        type = labelGroup.type,
                        position = labelGroup.position,
                        url = labelGroup.url,
                        styles = labelGroup.styles.map { style ->
                            MissionWidgetDataModel.LabelGroup.Styles(
                                key = style.key,
                                value = style.value,
                            )
                        }
                    )
                }
            )
        }
    }

    fun map4SquareMissionWidgetData(
        missionWidgetList: List<HomeMissionWidgetData.Mission>,
        isCache: Boolean,
        appLog: HomeMissionWidgetData.AppLog,
        channelName: String = "",
        channelId: String = "",
        header: ChannelHeader = ChannelHeader(),
        verticalPosition: Int = 0,
    ): List<Mission4SquareUiModel> {
        return mapMissionWidgetData(missionWidgetList, isCache, appLog)
            .take(4)
            .mapIndexed { index, model ->
                Mission4SquareWidgetMapper.map(
                    data = model,
                    cardPosition = index,
                    channelName = channelName,
                    channelId = channelId,
                    header = header,
                    verticalPosition = verticalPosition
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
