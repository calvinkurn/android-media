package com.tokopedia.tokochat.test.chatlist.robot

import com.tokopedia.tokochat.test.chatlist.robot.general.GeneralResult
import com.tokopedia.tokochat.test.chatlist.robot.state.StateResult

fun generalResult(func: GeneralResult.() -> Unit) = GeneralResult.apply(func)
fun stateResult(func: StateResult.() -> Unit) = StateResult.apply(func)
