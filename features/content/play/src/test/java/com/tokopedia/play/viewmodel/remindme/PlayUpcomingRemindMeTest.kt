package com.tokopedia.play.viewmodel.remindme

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayUpcomingInfoModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.uimodel.action.ClickRemindMeUpcomingChannel
import com.tokopedia.play.view.uimodel.event.RemindMeEvent
import com.tokopedia.play_common.domain.PlayChannelReminderUseCase
import com.tokopedia.play_common.domain.model.PlayReminder
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.fail

/**
 * Created By : Jonathan Darwin on September 09, 2021
 */
class PlayUpcomingRemindMeTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

//    @get:Rule
//    private val coroutineTestRule = CoroutineTestRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val upcomingInfoModelBuilder = PlayUpcomingInfoModelBuilder()
    private val mockUpcomingInfo = upcomingInfoModelBuilder.buildUpcomingInfo(isUpcoming = false)
    private val mockChannelData = channelDataBuilder.buildChannelData(
        upcomingInfo = mockUpcomingInfo
    )

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given a upcoming channel, when logged in user click remind me button, then upcoming info should be updated to reminded`() {
        val mockPlayChannelReminderUseCase: PlayChannelReminderUseCase = mockk(relaxed = true)
        val mockResponse = PlayReminder(
            PlayReminder.ToggleChannelReminder(
                header = PlayReminder.Header(
                    status = 200
                )
            )
        )

        coEvery { mockPlayChannelReminderUseCase.executeOnBackground() } returns mockResponse

        givenPlayViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickRemindMeUpcomingChannel)
        } thenVerify {
            val value = viewModel.observableUpcomingInfo.value
            value?.let {
                it.isEqualTo(mockUpcomingInfo.copy(isReminderSet = true))
            } ?: fail(Exception("No Upcoming Info"))
        }
    }

    @Test
    fun `given a upcoming channel, when logged in user click remind me button and error occur, then upcoming info is still the same`() {
        val mockPlayChannelReminderUseCase: PlayChannelReminderUseCase = mockk(relaxed = true)
        val mockResponse = PlayReminder(
            PlayReminder.ToggleChannelReminder(
                header = PlayReminder.Header(
                    status = 403
                )
            )
        )

        coEvery { mockPlayChannelReminderUseCase.executeOnBackground() } returns mockResponse

        givenPlayViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            setLoggedIn(true)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickRemindMeUpcomingChannel)
        } thenVerify {
            val value = viewModel.observableUpcomingInfo.value
            value?.let {
                it.isEqualTo(mockUpcomingInfo)
            } ?: fail(Exception("No Upcoming Info"))
        }
    }

    @Test
    fun `given a upcoming channel, when user is not logged in and click remind me button, then user cannot set reminder`() {
        givenPlayViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            setLoggedIn(false)
            createPage(mockChannelData)
            focusPage(mockChannelData)
        } andWhen {
            submitAction(ClickRemindMeUpcomingChannel)
        } thenVerify {
            val value = viewModel.observableUpcomingInfo.value
            value?.isReminderSet?.isFalse() ?: fail(Exception("No Upcoming Info"))
        }
    }
}