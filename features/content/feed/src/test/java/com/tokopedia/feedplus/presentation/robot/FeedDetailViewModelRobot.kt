package com.tokopedia.feedplus.presentation.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.feedplus.detail.FeedDetailViewModel
import com.tokopedia.feedplus.detail.data.FeedDetailRepository
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created by meyta.taliti on 10/09/23.
 */
class FeedDetailViewModelRobot(
    repository: FeedDetailRepository,
    private val dispatchers: CoroutineTestDispatchers
) : Closeable {

    val viewModel: FeedDetailViewModel = FeedDetailViewModel(repository)

    fun run(fn: suspend FeedDetailViewModelRobot.() -> Unit) {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
    }

    override fun close() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }
}

fun createFeedDetailViewModelRobot(
    repository: FeedDetailRepository = mockk(relaxed = true),
    dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    fn: FeedDetailViewModelRobot.() -> Unit = {}
): FeedDetailViewModelRobot {
    return FeedDetailViewModelRobot(repository, dispatchers).apply(fn)
}
