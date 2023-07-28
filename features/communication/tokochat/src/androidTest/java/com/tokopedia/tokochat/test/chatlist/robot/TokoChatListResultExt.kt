package com.tokopedia.tokochat.test.chatlist.robot

import com.tokopedia.tokochat.test.chatlist.robot.general.GeneralResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
