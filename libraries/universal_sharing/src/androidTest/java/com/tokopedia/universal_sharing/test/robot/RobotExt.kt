package com.tokopedia.universal_sharing.test.robot

import com.tokopedia.universal_sharing.test.robot.universalsharing.UniversalSharingRobot

fun universalSharingRobot(func: UniversalSharingRobot.() -> Unit): UniversalSharingRobot {
    return UniversalSharingRobot().apply(func)
}
