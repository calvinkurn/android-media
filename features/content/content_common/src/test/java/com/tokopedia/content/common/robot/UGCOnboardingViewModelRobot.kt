package com.tokopedia.content.common.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import com.tokopedia.content.common.onboarding.view.viewmodel.UGCOnboardingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class UGCOnboardingViewModelRobot(
    onboardingStrategy: UGCOnboardingStrategy,
    private val dispatcher: CoroutineTestDispatchers,
) : Closeable {

    val viewModel = UGCOnboardingViewModel(
        onboardingStrategy = onboardingStrategy,
    )

    fun setup(fn: suspend UGCOnboardingViewModelRobot.() -> Unit): UGCOnboardingViewModelRobot {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)

        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()

        return this
    }

    infix fun recordState(fn: suspend UGCOnboardingViewModelRobot.() -> Unit): UGCOnboardingUiState {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: UGCOnboardingUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    infix fun recordStateAsList(fn: suspend UGCOnboardingViewModelRobot.() -> Unit): List<UGCOnboardingUiState> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiStateList = mutableListOf<UGCOnboardingUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    infix fun recordEvent(fn: suspend UGCOnboardingViewModelRobot.() -> Unit): List<UGCOnboardingUiEvent> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiEventList = mutableListOf<UGCOnboardingUiEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEventList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEventList
    }

    infix fun recordStateAndEvent(fn: suspend UGCOnboardingViewModelRobot.() -> Unit): Pair<UGCOnboardingUiState, List<UGCOnboardingUiEvent>> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: UGCOnboardingUiState
        val uiEvents = mutableListOf<UGCOnboardingUiEvent>()
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvents.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState to uiEvents
    }

    fun start(fn: suspend UGCOnboardingViewModelRobot.() -> Unit) {
        use {
            runBlockingTest { fn() }
        }
    }

    suspend fun submitAction(action: UGCOnboardingAction) = act {
        viewModel.submitAction(action)
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}
