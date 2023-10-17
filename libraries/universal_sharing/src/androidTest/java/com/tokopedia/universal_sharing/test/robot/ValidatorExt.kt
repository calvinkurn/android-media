package com.tokopedia.universal_sharing.test.robot

import com.tokopedia.universal_sharing.test.robot.postpurchase.PostPurchaseResult
import com.tokopedia.universal_sharing.test.robot.postpurchase.PostPurchaseRobot
import com.tokopedia.universal_sharing.test.robot.universalsharing.UniversalSharingResult
import com.tokopedia.universal_sharing.test.robot.universalsharing.UniversalSharingRobot

fun universalSharingRobot(func: UniversalSharingRobot.() -> Unit): UniversalSharingRobot {
    return UniversalSharingRobot.apply(func)
}
infix fun UniversalSharingRobot.validate(func: UniversalSharingResult.() -> Unit): UniversalSharingResult {
    // in KYC, there is no manual CTA Button to upload, so we just wait
    Thread.sleep(1000)
    return UniversalSharingResult.apply(func)
}

fun postPurchaseRobot(func: PostPurchaseRobot.() -> Unit): PostPurchaseRobot {
    return PostPurchaseRobot.apply(func)
}
infix fun PostPurchaseRobot.validate(func: PostPurchaseResult.() -> Unit): PostPurchaseResult {
    return PostPurchaseResult.apply(func)
}
