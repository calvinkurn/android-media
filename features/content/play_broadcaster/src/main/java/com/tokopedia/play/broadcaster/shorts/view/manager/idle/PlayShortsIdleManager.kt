package com.tokopedia.play.broadcaster.shorts.view.manager.idle

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsIdleManager @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) {

    val eventBus = EventBus<State>()
    private var state: State = State.StandBy
    private var job: Job? = null

    fun startIdleTimer() {
        clear()
        job = CoroutineScope(dispatchers.computation).launch {
            emitState(State.StandBy)
            delay(IDLE_WAITING_DURATION)
            emitState(State.Idle)
        }
    }

    /**
     * TODO:
     * forceStandByMode should be called when:
     * 1. showMainComponent(false)
     * 2. bottomsheet is shown
     */
    fun forceStandByMode() {
        clear()
        emitState(State.StandBy)
    }

    fun forceIdleMode() {
        clear()
        emitState(State.Idle)
    }

    fun toggleState() {
        when (state) {
            State.StandBy -> {
                forceIdleMode()
            }
            State.Idle -> {
                startIdleTimer()
            }
        }
    }

    fun clear() {
        job?.cancel()
    }

    private fun emitState(state: State) {
        this.state = state
        eventBus.emit(this.state)
    }

    enum class State {
        StandBy, Idle
    }

    companion object {
        private const val IDLE_WAITING_DURATION = 3000L
    }
}
