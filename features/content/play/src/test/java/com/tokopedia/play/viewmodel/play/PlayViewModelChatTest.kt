package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.helper.NoValueException
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChatModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.PlayViewModelRobot2
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.assertEmpty
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.throwsException
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/02/21
 */
class PlayViewModelChatTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val dispatchers = rule.dispatchers

    private val chatBuilder = PlayChatModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()

    private val userSession: UserSessionInterface = mockk(relaxed = true)

    private val chatStreamsFactory = object : ChatStreams.Factory {
        override fun create(scope: CoroutineScope): ChatStreams {
            return ChatStreams(scope, dispatchers)
        }
    }

    @Test
    fun `given user is logged in, when send chat, chat should be sent`() {
        val message = "Hello World"
        val name = "User 1"
        val userId = "12345"

        val channelData = channelDataBuilder.buildChannelData(
            id = "121212"
        )

        every { userSession.name } returns name
        every { userSession.isLoggedIn } returns true
        every { userSession.userId } returns userId

        val robot = createPlayViewModelRobot(
            userSession = userSession,
            dispatchers = dispatchers,
            chatStreamsFactory = chatStreamsFactory,
        ) {
            createPage(channelData)
        }

        robot.use {
            it.sendChat(message)
            dispatchers.coroutineDispatcher.advanceTimeBy(500)
        }

        dispatchers.coroutineDispatcher.cancel()
        robot.viewModel.viewModelScope.cancel()

        robot.viewModel.chats.value.last()
            .assertEqualTo(
                chatBuilder.build(
                    messageId = "",
                    userId = userId,
                    name = name,
                    message = message,
                    isSelfMessage = true
                )
            )
    }

    @Test
    fun `given user is not logged in, when send chat, chat should not be sent`() {
        val message = "Hello World"

        val channelData = channelDataBuilder.buildChannelData(
            id = "121212"
        )

        every { userSession.isLoggedIn } returns false

        val robot = createPlayViewModelRobot(
            userSession = userSession,
            dispatchers = dispatchers,
            chatStreamsFactory = chatStreamsFactory,
        ) {
            createPage(channelData)
        }

        robot.use {
            it.sendChat(message)
            dispatchers.coroutineDispatcher.advanceTimeBy(500)
        }

        dispatchers.coroutineDispatcher.cancel()
        robot.viewModel.viewModelScope.cancel()

        robot.viewModel.chats.value.assertEmpty()
    }
}