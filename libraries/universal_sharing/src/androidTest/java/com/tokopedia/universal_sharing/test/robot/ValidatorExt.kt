package com.tokopedia.universal_sharing.test.robot

import com.tokopedia.universal_sharing.test.robot.universalsharing.UniversalSharingResult
import com.tokopedia.universal_sharing.test.robot.universalsharing.UniversalSharingRobot

infix fun UniversalSharingRobot.validate(func: UniversalSharingResult.() -> Unit): UniversalSharingResult {
    // in KYC, there is no manual CTA Button to upload, so we just wait
    Thread.sleep(1000)
    return UniversalSharingResult().apply(func)
}
