package com.tokopedia.topchat.chatlist.activity.robot

import com.tokopedia.topchat.chatlist.activity.robot.broadcast.BroadcastResult

fun broadcastResult(func: BroadcastResult.() -> Unit) = BroadcastResult.apply(func)
