package com.tokopedia.shareexperience.test.robot

import com.tokopedia.shareexperience.test.robot.channel.ChannelRobot
import com.tokopedia.shareexperience.test.robot.general.GeneralRobot
import com.tokopedia.shareexperience.test.robot.properties.PropertiesRobot

fun generalRobot(func: GeneralRobot.() -> Unit) = GeneralRobot.apply(func)
fun channelRobot(func: ChannelRobot.() -> Unit) = ChannelRobot.apply(func)
fun propertiesRobot(func: PropertiesRobot.() -> Unit) = PropertiesRobot.apply(func)
