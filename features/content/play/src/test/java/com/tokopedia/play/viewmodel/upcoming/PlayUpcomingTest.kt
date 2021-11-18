package com.tokopedia.play.viewmodel.upcoming

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.PlayReminder
import com.tokopedia.play.domain.PlayChannelReminderUseCase
import com.tokopedia.play.fake.FakePlayChannelSSE
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayUpcomingInfoModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.andWhenExpectEvent
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.robot.upcoming.createPlayUpcomingViewModelRobot
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.isEqualToIgnoringFields
import com.tokopedia.play.view.uimodel.action.ClickUpcomingButton
import com.tokopedia.play.view.uimodel.action.ImpressUpcomingChannel
import com.tokopedia.play.view.uimodel.event.PlayUpcomingUiEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.state.PlayUpcomingState
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on September 09, 2021
 */
class PlayUpcomingTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val upcomingInfoModelBuilder = PlayUpcomingInfoModelBuilder()
    private val mockUpcomingInfo = upcomingInfoModelBuilder.buildUpcomingInfo(isUpcoming = true)
    private val mockChannelData = channelDataBuilder.buildChannelData(
        upcomingInfo = mockUpcomingInfo
    )
    private val mockChannelDataWithNoUpcoming = channelDataBuilder.buildChannelData()

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockPlayNewAnalytic: PlayNewAnalytic = mockk(relaxed = true)
    private val fakePlayChannelSSE = FakePlayChannelSSE(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Impress Page
     */
    @Test
    fun `given a upcoming channel, user is open the upcoming page, then app should send impression analytic`() {
        /** Mock */
        every { mockPlayNewAnalytic.impressUpcomingPage(mockChannelData.id) } returns Unit

        /** Verify */
        val robot = createPlayUpcomingViewModelRobot(
            playAnalytic = mockPlayNewAnalytic
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            runBlockingTest {
                robot.submitAction(ImpressUpcomingChannel)
            }

            /** Verify */
            verify { mockPlayNewAnalytic.impressUpcomingPage(mockChannelData.id) }
        }
    }


    /**
     * Remind Me
     */
    @Test
    fun `given a upcoming channel, when logged in user click remind me button, then upcoming state should be updated to reminded`() {
        /** Mock */
        val mockPlayChannelReminderUseCase: PlayChannelReminderUseCase = mockk(relaxed = true)
        val mockResponse = PlayReminder(
            PlayReminder.ToggleChannelReminder(
                header = PlayReminder.Header(
                    status = 200
                )
            )
        )
        val mockEvent = PlayUpcomingUiEvent.RemindMeEvent(
            message = UiString.Resource(1),
            isSuccess = true
        )

        coEvery { mockPlayChannelReminderUseCase.executeOnBackground() } returns mockResponse
        every { mockPlayNewAnalytic.clickRemindMe(mockChannelData.id) } returns Unit
        every { mockUserSession.isLoggedIn } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test **/
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify */
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.Reminded)
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.RemindMeEvent::message)
        }
    }

    @Test
    fun `given a upcoming channel, when logged in user click remind me button and error occur, then upcoming state is still the same`() {
        /** Mock */
        val mockPlayChannelReminderUseCase: PlayChannelReminderUseCase = mockk(relaxed = true)
        val mockResponse = PlayReminder(
            PlayReminder.ToggleChannelReminder(
                header = PlayReminder.Header(
                    status = 403
                )
            )
        )
        val mockEvent = PlayUpcomingUiEvent.RemindMeEvent(
            message = UiString.Resource(1),
            isSuccess = false
        )

        coEvery { mockPlayChannelReminderUseCase.executeOnBackground() } returns mockResponse
        every { mockPlayNewAnalytic.clickRemindMe(mockChannelData.id) } returns Unit
        every { mockUserSession.isLoggedIn } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test **/
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify */
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.RemindMe)
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.RemindMeEvent::message)
        }
    }

    @Test
    fun `given a upcoming channel, when user is not logged in and click remind me button, then user cannot set reminder`() {
        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            applink = ApplinkConst.LOGIN,
            requestCode = 123
        )

        every { mockPlayNewAnalytic.clickRemindMe(mockChannelData.id) } returns Unit
        every { mockUserSession.isLoggedIn } returns false

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test **/
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify */
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.Loading)
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.OpenPageEvent::requestCode)
        }
    }

//    /**
//     * Watch Now
//     */
//    @Test
//    fun `given a upcoming channel, when channel already live and user click button, then app should send analytic and close SSE`() {
//        every { mockPlayNewAnalytic.clickWatchNow(mockChannelData.id) } returns Unit
//
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playAnalytic = mockPlayNewAnalytic,
//            playChannelSSE = fakePlayChannelSSE
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            submitAction(ClickWatchNowUpcomingChannel)
//        } thenVerify {
//            verify { mockPlayNewAnalytic.clickWatchNow(mockChannelData.id) }
//            fakePlayChannelSSE.isConnectionOpen().assertFalse()
//        }
//    }
//
//    /**
//     * SSE
//     */
//    @Test
//    fun `given a upcoming channel, when app displaying upcoming channel, then app should automatically connect to SSE`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } thenVerify {
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(true)
//        }
//    }
//
//    @Test
//    fun `given a non-upcoming channel, when app displaying upcoming channel, then app should not connect to SSE`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelDataWithNoUpcoming)
//            focusPage(mockChannelDataWithNoUpcoming)
//        } thenVerify {
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(false)
//        }
//    }
//
//    @Test
//    fun `given a upcoming channel, when channel get message that the channel is already alive, then it should update the channel upcoming info`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            fakePlayChannelSSE.fakeSendMessage("upcommingchannelupdatelive", "{channel_id: 1}")
//        } thenVerify {
//            viewModel.observableUpcomingInfo.value?.isAlreadyLive.isEqualTo(true)
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(false)
//        }
//    }
//
//    @Test
//    fun `given a upcoming channel, when channel get message that the channel is already active, then it should update the channel upcoming info`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE,
//            playAnalytic = mockPlayNewAnalytic
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            fakePlayChannelSSE.fakeSendMessage("upcommingchannelupdateactive", "{channel_id: 1}")
//        } thenVerify {
//            viewModel.observableUpcomingInfo.value?.isAlreadyLive.isEqualTo(true)
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(false)
//        }
//    }
//
//    @Test
//    fun `given a upcoming channel, when channel get unrecognized event, then it should do nothing`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE,
//            playAnalytic = mockPlayNewAnalytic
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            fakePlayChannelSSE.fakeSendMessage("unrecognizedevent", "{channel_id: 1}")
//        } thenVerify {
//            viewModel.observableUpcomingInfo.value?.isEqualTo(mockUpcomingInfo)
//        }
//    }
//
//    @Test
//    fun `given a upcoming channel, when channel get different channel_id alive, then it should do nothing with current channel`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE,
//            playAnalytic = mockPlayNewAnalytic
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            fakePlayChannelSSE.fakeSendMessage("unrecognizedevent", "{channel_id: 12312}")
//        } thenVerify {
//            viewModel.observableUpcomingInfo.value?.isAlreadyLive.isEqualTo(false)
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(true)
//        }
//    }
//
//    @Test
//    fun `given a upcoming channel, when channel is defocused, then it should disconnect the SSE`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE,
//            playAnalytic = mockPlayNewAnalytic
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            defocusPage(true)
//        } thenVerify {
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(false)
//        }
//    }
//
//    @Test
//    fun `given a upcoming channel, when SSE is intended to be closed, then it should reconnect to SSE`() {
//        givenPlayViewModelRobot(
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playChannelSSE = fakePlayChannelSSE,
//            playAnalytic = mockPlayNewAnalytic
//        ) {
//            setLoggedIn(false)
//            createPage(mockChannelData)
//            focusPage(mockChannelData)
//        } andWhen {
//            fakePlayChannelSSE.close()
//        } thenVerify {
//            fakePlayChannelSSE.isConnectionOpen().isEqualTo(false)
//        }
//    }
}