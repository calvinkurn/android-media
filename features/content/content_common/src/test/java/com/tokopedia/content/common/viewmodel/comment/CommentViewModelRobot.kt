package com.tokopedia.content.common.viewmodel.comment

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.comment.CommentAction
import com.tokopedia.content.common.comment.CommentEvent
import com.tokopedia.content.common.comment.ContentCommentViewModel
import com.tokopedia.content.common.comment.PageSource
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.uimodel.CommentParam
import com.tokopedia.content.common.comment.uimodel.CommentWidgetUiModel
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
    pageSource: PageSource,
    repository: ContentCommentRepository,
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
        dispatchers.coroutineDispatcher.advanceUntilIdle()
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
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return comments
    }

    fun recordQuery(fn: suspend CommentViewModelRobot.() -> Unit): CommentParam {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var queries: CommentParam
        scope.launch {
            val query = vm.getPrivateField<MutableStateFlow<CommentParam>>("_query")
            query.collect {
                queries = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return queries
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
    pageSource: PageSource = PageSource.Play("12665"),
    repository: ContentCommentRepository = mockk(relaxed = true),
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
