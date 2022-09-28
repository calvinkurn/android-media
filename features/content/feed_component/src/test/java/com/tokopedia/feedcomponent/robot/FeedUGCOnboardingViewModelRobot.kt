package com.tokopedia.feedcomponent.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.feedcomponent.onboarding.view.viewmodel.FeedUGCOnboardingViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class FeedUGCOnboardingViewModelRobot(
    private val username: String = "",
    private val onboardingStrategy: FeedUGCOnboardingStrategy,
    private val dispatcher: CoroutineTestDispatchers,
) : Closeable {

    val viewModel = FeedUGCOnboardingViewModel(
        username = username,
        onboardingStrategy = onboardingStrategy,
    )

    fun setup(fn: suspend FeedUGCOnboardingViewModelRobot.() -> Unit): FeedUGCOnboardingViewModelRobot {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)

        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()

        return this
    }

    infix fun recordState(fn: suspend FeedUGCOnboardingViewModelRobot.() -> Unit): FeedUGCOnboardingUiState {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: FeedUGCOnboardingUiState
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

    infix fun recordStateAsList(fn: suspend FeedUGCOnboardingViewModelRobot.() -> Unit): List<FeedUGCOnboardingUiState> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiStateList = mutableListOf<FeedUGCOnboardingUiState>()
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

    infix fun recordEvent(fn: suspend FeedUGCOnboardingViewModelRobot.() -> Unit): List<FeedUGCOnboardingUiEvent> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiEventList = mutableListOf<FeedUGCOnboardingUiEvent>()
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

    infix fun recordStateAndEvent(fn: suspend FeedUGCOnboardingViewModelRobot.() -> Unit): Pair<FeedUGCOnboardingUiState, List<FeedUGCOnboardingUiEvent>> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: FeedUGCOnboardingUiState
        val uiEvents = mutableListOf<FeedUGCOnboardingUiEvent>()
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

    fun start(fn: suspend FeedUGCOnboardingViewModelRobot.() -> Unit) {
        use {
            runBlockingTest { fn() }
        }
    }

    suspend fun submitAction(action: FeedUGCOnboardingAction) = act {
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