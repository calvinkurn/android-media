package com.tokopedia.play.broadcaster.shorts.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.robot.PlayBroProductSetupViewModelRobot
import com.tokopedia.play.broadcaster.setup.product.model.PlayBroProductChooserEvent
import com.tokopedia.play.broadcaster.setup.product.model.ProductChooserUiState
import com.tokopedia.play.broadcaster.shorts.domain.PlayShortsRepository
import com.tokopedia.play.broadcaster.shorts.domain.manager.PlayShortsAccountManager
import com.tokopedia.play.broadcaster.shorts.ui.model.action.PlayShortsAction
import com.tokopedia.play.broadcaster.shorts.ui.model.event.PlayShortsUiEvent
import com.tokopedia.play.broadcaster.shorts.ui.model.state.PlayShortsUiState
import com.tokopedia.play.broadcaster.shorts.view.viewmodel.PlayShortsViewModel
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play_common.shortsuploader.PlayShortsUploader
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsViewModelRobot(
    repo: PlayShortsRepository = mockk(relaxed = true),
    sharedPref: HydraSharedPreferences = mockk(relaxed = true),
    accountManager: PlayShortsAccountManager = mockk(relaxed = true),
    playShortsUploader: PlayShortsUploader = mockk(relaxed = true),
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
) : Closeable {

    private val viewModel = PlayShortsViewModel(
        repo = repo,
        sharedPref = sharedPref,
        accountManager = accountManager,
        playShortsUploader = playShortsUploader,
    )

    val isAllMandatoryMenuChecked: Boolean
        get() = viewModel.isAllMandatoryMenuChecked

    val isFormFilled: Boolean
        get() = viewModel.isFormFilled

    val isAllowChangeAccount: Boolean
        get() = viewModel.isAllowChangeAccount

    val tncList: List<TermsAndConditionUiModel>
        get() = viewModel.tncList

    fun setUp(fn: PlayShortsViewModelRobot.() -> Unit): PlayShortsViewModelRobot {
        fn()
        return this
    }

    fun recordState(fn: suspend PlayShortsViewModelRobot.() -> Unit): PlayShortsUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayShortsUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordStates(fn: suspend PlayShortsViewModelRobot.() -> Unit): List<PlayShortsUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        var uiState = mutableListOf<PlayShortsUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiState.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordEvent(fn: suspend PlayShortsViewModelRobot.() -> Unit): List<PlayShortsUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEventList = mutableListOf<PlayShortsUiEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEventList.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEventList
    }

    fun recordStateAndEvent(fn: suspend PlayShortsViewModelRobot.() -> Unit): Pair<PlayShortsUiState, List<PlayShortsUiEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayShortsUiState
        val uiEvents = mutableListOf<PlayShortsUiEvent>()
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
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState to uiEvents
    }

    fun submitAction(action: PlayShortsAction) {
        viewModel.submitAction(action)
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    private fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}
