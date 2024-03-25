package com.tokopedia.content.common.viewmodel.comment

import androidx.lifecycle.viewModelScope
import com.tokopedia.feed.common.comment.CommentAction
import com.tokopedia.feed.common.comment.CommentEvent
import com.tokopedia.feed.common.comment.ContentCommentViewModel
import com.tokopedia.feed.common.comment.PageSource
import com.tokopedia.feed.common.comment.repository.ContentCommentRepository
import com.tokopedia.feed.common.comment.uimodel.CommentParam
import com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * @author by astidhiyaa on 21/03/23
 */
class CommentViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers,
    private val userSession: UserSessionInterface,
    pageSource: com.tokopedia.feed.common.comment.PageSource,
    repository: com.tokopedia.feed.common.comment.repository.ContentCommentRepository,
) : Closeable {

    override fun close() {
        vm.viewModelScope.coroutineContext.cancelChildren()
    }

    val vm = com.tokopedia.feed.common.comment.ContentCommentViewModel(
        source = pageSource,
        repo = repository,
        userSession = userSession
    )

    suspend fun submitAction(act: com.tokopedia.feed.common.comment.CommentAction) = act {
        vm.submitAction(act)
    }

    fun recordEvent(fn: suspend CommentViewModelRobot.() -> Unit): List<com.tokopedia.feed.common.comment.CommentEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val events = mutableListOf<com.tokopedia.feed.common.comment.CommentEvent>()
        scope.launch {
            vm.event.collect {
                events.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return events
    }

    fun recordComments(fn: suspend CommentViewModelRobot.() -> Unit): com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var comments: com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel
        scope.launch {
            vm.comments.collect {
                comments = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return comments
    }

    fun recordQueries(fn: suspend CommentViewModelRobot.() -> Unit): List<com.tokopedia.feed.common.comment.uimodel.CommentParam> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val queries = mutableListOf<com.tokopedia.feed.common.comment.uimodel.CommentParam>()
        scope.launch {
            val query = vm.getPrivateField<MutableStateFlow<com.tokopedia.feed.common.comment.uimodel.CommentParam>>("_query")
            query.collect {
                queries.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return queries
    }

    fun recordQueryAndComment(fn: suspend CommentViewModelRobot.() -> Unit): Pair<com.tokopedia.feed.common.comment.uimodel.CommentParam, com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var query: com.tokopedia.feed.common.comment.uimodel.CommentParam
        lateinit var comment: com.tokopedia.feed.common.comment.uimodel.CommentWidgetUiModel
        scope.launch {
            val q = vm.getPrivateField<MutableStateFlow<com.tokopedia.feed.common.comment.uimodel.CommentParam>>("_query")
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
        dispatchers.coroutineDispatcher.advanceUntilIdle()
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
    pageSource: com.tokopedia.feed.common.comment.PageSource = com.tokopedia.feed.common.comment.PageSource.Play("12665"),
    repository: com.tokopedia.feed.common.comment.repository.ContentCommentRepository = mockk(relaxed = true),
    fn: CommentViewModelRobot.() -> Unit = {},
): CommentViewModelRobot {
    return CommentViewModelRobot(
        dispatchers, userSession, pageSource, repository
    ).apply(fn)
}

internal fun <T> Any.getPrivateField(name: String): T {
    val field = this.javaClass.getDeclaredField(name)
    field.isAccessible = true
    return field.get(this) as T
}
