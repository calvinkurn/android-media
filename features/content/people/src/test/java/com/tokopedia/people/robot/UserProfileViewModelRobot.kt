package com.tokopedia.people.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.viewmodels.UserProfileViewModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on July 05, 2022
 */
class UserProfileViewModelRobot(
    private val username: String = "",
    private val repo: UserProfileRepository = mockk(relaxed = true),
    private val userSession: UserSessionInterface = mockk(relaxed = true),
    private val dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
) : Closeable {

    val viewModel = UserProfileViewModel(
        username = username,
        repo = repo,
        userSession = userSession,
    )

    fun setup(fn: suspend UserProfileViewModelRobot.() -> Unit): UserProfileViewModelRobot {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)

        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()

        return this
    }

    infix fun recordState(fn: suspend UserProfileViewModelRobot.() -> Unit): UserProfileUiState {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: UserProfileUiState
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

    infix fun recordStateAsList(fn: suspend UserProfileViewModelRobot.() -> Unit): List<UserProfileUiState> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiStateList = mutableListOf<UserProfileUiState>()
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

    infix fun recordEvent(fn: suspend UserProfileViewModelRobot.() -> Unit): List<UserProfileUiEvent> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiEventList = mutableListOf<UserProfileUiEvent>()
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

    infix fun recordStateAndEvent(fn: suspend UserProfileViewModelRobot.() -> Unit): Pair<UserProfileUiState, List<UserProfileUiEvent>> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: UserProfileUiState
        val uiEvents = mutableListOf<UserProfileUiEvent>()
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

    fun start(fn: suspend UserProfileViewModelRobot.() -> Unit) {
        use {
            runBlockingTest { fn() }
        }
    }

    suspend fun submitAction(action: UserProfileAction) = act {
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
