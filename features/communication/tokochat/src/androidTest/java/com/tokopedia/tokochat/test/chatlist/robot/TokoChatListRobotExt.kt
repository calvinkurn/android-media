package com.tokopedia.tokochat.test.chatlist.robot

import com.tokopedia.tokochat.test.chatlist.robot.general.GeneralRobot
import com.tokopedia.tokochat.test.chatlist.robot.state.StateRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
fun stateRobot(func: StateRobot.() -> Unit) = StateRobot.apply(func)
