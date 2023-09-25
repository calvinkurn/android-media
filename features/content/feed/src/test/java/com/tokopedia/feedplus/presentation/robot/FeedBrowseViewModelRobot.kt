package com.tokopedia.feedplus.presentation.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.FeedBrowseViewModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created by meyta.taliti on 08/09/23.
 */
class FeedBrowseViewModelRobot(
    repository: FeedBrowseRepository,
    private val dispatchers: CoroutineTestDispatchers
) : Closeable {

    val viewModel: FeedBrowseViewModel = FeedBrowseViewModel(repository)

    fun recordState(fn: suspend FeedBrowseViewModelRobot.() -> Unit): FeedBrowseUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: FeedBrowseUiState
        scope.launch {
            viewModel.uiState.collectLatest {
                uiState = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    override fun close() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }
}

fun createFeedBrowseViewModelRobot(
    repository: FeedBrowseRepository = mockk(relaxed = true),
    dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    fn: FeedBrowseViewModelRobot.() -> Unit = {}
): FeedBrowseViewModelRobot {
    return FeedBrowseViewModelRobot(repository, dispatchers).apply(fn)
}
