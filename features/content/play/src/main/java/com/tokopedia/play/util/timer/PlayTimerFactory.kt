package com.tokopedia.play.util.timer

import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 16/09/21
 */
class PlayTimerFactory @Inject constructor() : TimerFactory {

    override suspend fun createLoopingAlarm(
        millisInFuture: Long,
        stopCondition: suspend () -> Boolean,
        onStart: suspend () -> Unit,
        onFinish: suspend () -> Unit
    ) {
        while(true) {
            if (stopCondition()) break
            onStart()
            delay(millisInFuture)
            onFinish()
        }
    }
}