package com.tokopedia.notifcenter.test.robot

import com.tokopedia.notifcenter.test.robot.detail.DetailRobot
import com.tokopedia.notifcenter.test.robot.filter.FilterRobot
import com.tokopedia.notifcenter.test.robot.general.GeneralRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
fun detailRobot(func: DetailRobot.() -> Unit) = DetailRobot.apply(func)
fun filterRobot(func: FilterRobot.() -> Unit) = FilterRobot.apply(func)
