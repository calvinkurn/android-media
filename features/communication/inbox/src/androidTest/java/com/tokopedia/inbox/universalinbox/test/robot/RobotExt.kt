package com.tokopedia.inbox.universalinbox.test.robot

import com.tokopedia.inbox.universalinbox.test.robot.general.GeneralRobot
import com.tokopedia.inbox.universalinbox.test.robot.menu.MenuRobot
import com.tokopedia.inbox.universalinbox.test.robot.recommendation.RecommendationRobot
import com.tokopedia.inbox.universalinbox.test.robot.widget.WidgetRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
fun menuRobot(func: MenuRobot.() -> Unit) = MenuRobot.apply(func)
fun widgetRobot(func: WidgetRobot.() -> Unit) = WidgetRobot.apply(func)
fun recommendationRobot(func: RecommendationRobot.() -> Unit) = RecommendationRobot.apply(func)
