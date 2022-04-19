package com.tokopedia.play.util.timer

/**
 * Created by jegul on 16/09/21
 */
interface TimerFactory {

    suspend fun createLoopingAlarm(
        millisInFuture: Long,
        stopCondition: suspend () -> Boolean,
        onStart: suspend () -> Unit = {},
        onFinish: suspend () -> Unit = {}
    )
}