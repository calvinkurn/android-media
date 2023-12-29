package com.tokopedia.people.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.viewmodels.UserProfileSettingsViewModel
import com.tokopedia.people.views.uimodel.ProfileSettingsUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileSettingsAction
import com.tokopedia.people.views.uimodel.event.UserProfileSettingsEvent
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on June 05, 2023
 */
class UserProfileSettingsViewModelRobot(
    private val userID: String = "123",
    private val repo: UserProfileRepository = mockk(relaxed = true),
    private val dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
) : Closeable {

    val viewModel = UserProfileSettingsViewModel(
        userID = userID,
        repo = repo,
    )

    fun setup(fn: suspend UserProfileSettingsViewModelRobot.() -> Unit): UserProfileSettingsViewModelRobot {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)

        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()

        return this
    }

    infix fun recordReviewSettingsState(fn: suspend UserProfileSettingsViewModelRobot.() -> Unit): ProfileSettingsUiModel {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: ProfileSettingsUiModel
        scope.launch {
            viewModel.reviewSettings.collect {
                uiState = it
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    infix fun recordReviewSettingsStateAsList(fn: suspend UserProfileSettingsViewModelRobot.() -> Unit): List<ProfileSettingsUiModel> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiStateList = mutableListOf<ProfileSettingsUiModel>()
        scope.launch {
            viewModel.reviewSettings.collect {
                uiStateList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    infix fun recordEvent(fn: suspend UserProfileSettingsViewModelRobot.() -> Unit): List<UserProfileSettingsEvent> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiEventList = mutableListOf<UserProfileSettingsEvent>()
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

    fun start(fn: suspend UserProfileSettingsViewModelRobot.() -> Unit) {
        use {
            runTest { fn() }
        }
    }

    suspend fun submitAction(action: UserProfileSettingsAction) = act {
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
