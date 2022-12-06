package com.tokopedia.play.broadcaster.shorts.robot

import androidx.lifecycle.viewModelScope
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

    suspend fun submitAction(action: PlayShortsAction) = act {
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
