package com.tokopedia.tokochat.test.chatlist.robot

import com.tokopedia.tokochat.test.chatlist.robot.general.GeneralRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
