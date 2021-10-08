package com.tokopedia.home.beranda.data.mapper

import com.tokopedia.home.beranda.domain.model.recharge_recommendation.RechargeRecommendation
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidget
import com.tokopedia.home.beranda.domain.model.salam_widget.SalamWidgetData
import com.tokopedia.home_component.model.ReminderData
import com.tokopedia.home_component.model.ReminderWidget

object ReminderWidgetMapper {
    fun mapperRechargetoReminder(recharge : RechargeRecommendation): ReminderWidget {
        recharge.recommendations.first().let {
            return ReminderWidget(
                    it.contentID,
                    listOf(
                            ReminderData(
                                    appLink = it.applink,
                                    backgroundColor = listOf(it.backgroundColor),
                                    buttonText = it.buttonText,
                                    id = it.contentID,
                                    iconURL = it.iconURL,
                                    link = it.link,
                                    mainText = it.mainText,
                                    subText = it.subText,
                                    title = it.title,
                                    UUID = recharge.UUID
                            )
                    )
            )
        }
    }

    fun mapperSalamtoReminder(salam : SalamWidget): ReminderWidget {
        salam.salamWidget.let {
            return ReminderWidget(
                    it.id.toString(),
                    listOf(
                            ReminderData(
                                    appLink = it.appLink,
                                    backgroundColor = listOf(it.backgroundColor),
                                    buttonText = it.buttonText,
                                    id = it.id.toString(),
                                    iconURL = it.iconURL,
                                    link = it.link,
                                    mainText = it.mainText,
                                    subText = it.subText,
                                    title = it.title
                            )
                    )
            )
        }
    }
}