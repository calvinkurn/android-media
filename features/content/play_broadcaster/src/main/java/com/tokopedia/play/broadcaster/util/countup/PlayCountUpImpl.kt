package com.tokopedia.play.broadcaster.util.countup

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 26, 2021
 */
class PlayCountUpImpl @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
): PlayCountUp {

    private val scope = CoroutineScope(dispatcher.io)
    private var job: Job? = null
    private var listener: PlayCountUpListener? = null

    override fun setListener(listener: PlayCountUpListener) {
        this.listener = listener
    }

    override fun start(duration: Long, maxDuration: Long) {
        var currentDuration = duration

        job?.cancel()
        job = scope.launch {
            while(currentDuration < maxDuration && isActive) {

                delay(DEFAULT_INTERVAL)
                currentDuration += DEFAULT_INTERVAL

                withContext(dispatcher.main) {
                    listener?.onTick(currentDuration)
                }
            }

            if (isActive) {
                withContext(dispatcher.main) { listener?.onFinish() }
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
        listener = null
    }

    companion object {
        private const val DEFAULT_INTERVAL = 1000L
    }
}