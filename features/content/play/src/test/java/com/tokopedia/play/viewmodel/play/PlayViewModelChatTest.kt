package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChatModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/02/21
 */
class PlayViewModelChatTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val chatBuilder = PlayChatModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()

    private val classBuilder = ClassBuilder()
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val modelMapper = classBuilder.getPlayUiModelMapper(
            userSession = userSession
    )

    @Test
    fun `given user is logged in, when send chat, chat should be sent`() {
        val message = "Hello World"
        val name = "User 1"
        val userId = "12345"

        val channelData = channelDataBuilder.buildChannelData(
                id = "121212"
        )

        givenPlayViewModelRobot(
                userSession = userSession,
                playUiModelMapper = modelMapper
        ) {
            setLoggedIn(true)
            setName(name)
            setUserId(userId)

            createPage(channelData)
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

        val channelData = channelDataBuilder.buildChannelData(
                id = "121212"
        )

        givenPlayViewModelRobot(
                userSession = userSession,
                playUiModelMapper = modelMapper
        ) {
            setLoggedIn(false)

            createPage(channelData)
        } andWhen {
            sendChat(message)
        } thenVerify {
            newChatResult
                    .hasNoValue()
        }
    }
}