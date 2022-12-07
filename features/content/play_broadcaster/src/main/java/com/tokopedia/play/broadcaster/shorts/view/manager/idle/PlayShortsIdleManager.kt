package com.tokopedia.play.broadcaster.shorts.view.manager.idle

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 11, 2022
 */
class PlayShortsIdleManager @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) {

    private val _state = MutableStateFlow(State.Unknown)
    val state: Flow<State>
        get() = _state
    private var job: Job? = null

    fun startIdleTimer(scope: CoroutineScope) {
        clear()
        job = scope.launch(dispatchers.computation) {
            emitState(State.StandBy)
            delay(IDLE_WAITING_DURATION)
            emitState(State.Idle)
        }
    }

    fun forceStandByMode() {
        clear()
        emitState(State.StandBy)
    }

    private fun forceIdleMode() {
        clear()
        emitState(State.Idle)
    }

    fun toggleState(scope: CoroutineScope) {
        when (_state.value) {
            State.StandBy -> {
                forceIdleMode()
            }
            State.Idle -> {
                startIdleTimer(scope)
            }
        }
    }

    fun clear() {
        job?.cancel()
    }

    private fun emitState(newState: State) {
        _state.update { newState }
    }

    enum class State {
        Unknown, StandBy, Idle
    }

    companion object {
        private const val IDLE_WAITING_DURATION = 3000L
    }
}
