package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.PlayChatModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/02/21
 */
class PlayViewModelChatTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val chatBuilder = PlayChatModelBuilder()

    private val classBuilder = ClassBuilder()
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val modelMapper = classBuilder.getPlayUiModelMapper(
            userSession = userSession
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given user is logged in, when send chat, chat should be sent`() {
        val message = "Hello World"
        val name = "User 1"
        val userId = "12345"

        givenPlayViewModelRobot(
                userSession = userSession,
                playUiModelMapper = modelMapper
        ) {
            setLoggedIn(true)
            setName(name)
            setUserId(userId)
        } andWhen {
            sendChat(message)
        } thenVerify {
            newChatResult
                    .isEqualTo(
                        chatBuilder.build(
                                messageId = "",
                                userId = userId,
                                name = name,
                                message = message,
                                isSelfMessage = true
                        )
                    )
        }
    }

    @Test
    fun `given user is not logged in, when send chat, chat should not be sent`() {
        val message = "Hello World"

        givenPlayViewModelRobot(
                userSession = userSession,
                playUiModelMapper = modelMapper
        ) {
            setLoggedIn(false)
        } andWhen {
            sendChat(message)
        } thenVerify {
            newChatResult
                    .hasNoValue()
        }
    }
}