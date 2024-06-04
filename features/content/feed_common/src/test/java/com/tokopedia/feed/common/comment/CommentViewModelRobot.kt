package com.tokopedia.feed.common.comment

import androidx.lifecycle.viewModelScope
import com.tokopedia.feed.common.comment.repository.ContentCommentRepository
import com.tokopedia.feed.common.comment.uimodel.CommentParam
import com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.yield
import java.io.Closeable

/**
 * @author by astidhiyaa on 21/03/23
 */
class CommentViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers,
    private val userSession: UserSessionInterface,
    pageSource: PageSource,
    repository: ContentCommentRepository
) : Closeable {

    override fun close() {
        vm.viewModelScope.coroutineContext.cancelChildren()
    }

    val vm = ContentCommentViewModel(
        source = pageSource,
        repo = repository,
        userSession = userSession
    )

    suspend fun submitAction(act: CommentAction) = act {
        vm.submitAction(act)
    }

    fun recordEvent(fn: suspend CommentViewModelRobot.() -> Unit): List<CommentEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val events = mutableListOf<CommentEvent>()
        scope.launch {
            vm.event.collect {
                events.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return events
    }

    fun recordComments(fn: suspend CommentViewModelRobot.() -> Unit): CommentWidgetUiModel {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var comments: CommentWidgetUiModel
        scope.launch {
            vm.comments.collect {
                comments = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return comments
    }

    fun recordQueries(fn: suspend CommentViewModelRobot.() -> Unit): List<CommentParam> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val queries = mutableListOf<CommentParam>()
        scope.launch {
            val query =
                vm.getPrivateField<MutableStateFlow<CommentParam>>(
                    "_query"
                )
            query.collect {
                queries.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return queries
    }

    fun recordQueryAndComment(fn: suspend CommentViewModelRobot.() -> Unit): Pair<CommentParam, CommentWidgetUiModel> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var query: CommentParam
        lateinit var comment: CommentWidgetUiModel
        scope.launch {
            val q =
                vm.getPrivateField<MutableStateFlow<CommentParam>>(
                    "_query"
                )
            q.collect {
                query = it
            }
        }
        scope.launch {
            vm.comments.collect {
                comment = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.scheduler.advanceUntilIdle()
        scope.cancel()
        return Pair(query, comment)
    }

    fun setLogin(isLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isLoggedIn
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }
}

internal fun createCommentRobot(
    dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    userSession: UserSessionInterface = mockk(relaxed = true),
    pageSource: PageSource = PageSource.Play(
        "12665"
    ),
    repository: ContentCommentRepository = mockk(
        relaxed = true
    ),
    fn: CommentViewModelRobot.() -> Unit = {}
): CommentViewModelRobot {
    return CommentViewModelRobot(
        dispatchers,
        userSession,
        pageSource,
        repository
    ).apply(fn)
}

internal fun <T> Any.getPrivateField(name: String): T {
    val field = this.javaClass.getDeclaredField(name)
    field.isAccessible = true
    return field.get(this) as T
}
