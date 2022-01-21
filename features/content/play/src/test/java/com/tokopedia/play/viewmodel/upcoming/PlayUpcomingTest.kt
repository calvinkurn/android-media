package com.tokopedia.play.viewmodel.upcoming

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.play.data.PlayReminder
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.PlayChannelReminderUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.fake.FakePlayChannelSSE
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayPartnerInfoModelBuilder
import com.tokopedia.play.model.PlayUpcomingInfoModelBuilder
import com.tokopedia.play.robot.upcoming.createPlayUpcomingViewModelRobot
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.*
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.PlayUpcomingUiEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
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
    private val mockChannelDataWithBuyerPartner = channelDataBuilder.buildChannelData(
        upcomingInfo = mockUpcomingInfo,
        partnerInfo = PlayPartnerInfoModelBuilder().buildPlayPartnerInfo(
            type = PartnerType.Buyer
        )
    )

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockPlayNewAnalytic: PlayNewAnalytic = mockk(relaxed = true)
    private val mockGetChannelStatus: GetChannelStatusUseCase = mockk(relaxed = true)
    private val mockPlayUiModelMapper: PlayUiModelMapper = mockk(relaxed = true)
    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)
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
            verify { mockPlayNewAnalytic.clickRemindMe(any()) }

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
            verify { mockPlayNewAnalytic.clickRemindMe(any()) }

            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.RemindMe)
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.RemindMeEvent::message)
        }
    }

    @Test
    fun `given a upcoming channel, when user is not logged in and click remind me button, then user cannot set reminder`() {
        /** Mock */
        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            applink = ApplinkConst.LOGIN,
            requestCode = 123
        )

        every { mockUserSession.isLoggedIn } returns false

        /** Prepare */
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

    /**
     * Watch Now
     */
    @Test
    fun `given a upcoming channel, when channel already live and user click button, then app should send analytic and close SSE`() {
        /** Mock */
        every { mockPlayNewAnalytic.clickWatchNow(mockChannelData.id) } returns Unit

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
            viewModel.startSSE(mockChannelData.id)
            fakePlayChannelSSE.fakeSendMessage("upcommingchannelupdatelive", "{ \"channel_id\" : ${mockChannelData.id} }")
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify **/
            fakePlayChannelSSE.isConnectionOpen().assertFalse()

            verify { mockPlayNewAnalytic.clickWatchNow(mockChannelData.id) }

            events.assertNotEmpty()
            events.last().isEqualTo(PlayUpcomingUiEvent.RefreshChannelEvent)
        }
    }

    /**
     * Share Link
     */
    @Test
    fun `given a upcoming channel, when user click share link, then it should emit copy clipboard & show info event`() {
        /** Mock */
        val mockClipboardEvent = PlayUpcomingUiEvent.CopyToClipboardEvent(mockChannelData.channelDetail.shareInfo.content)
        val mockInfoEvent = PlayUpcomingUiEvent.ShowInfoEvent(UiString.Resource(1))

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickShareUpcomingAction)
            }

            /** Verify **/
            events.assertNotEmpty()
            events[0].isEqualTo(mockClipboardEvent)
            events[1].isEqualToIgnoringFields(mockInfoEvent, PlayUpcomingUiEvent.ShowInfoEvent::message)
        }
    }

    /**
     * Click Partner Name
     */
    @Test
    fun `given a upcoming channel, when user click partner name and partner is shop, then it should emit open page event`() {
        /** Mock */
        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            ApplinkConst.SHOP,
            listOf(mockChannelData.partnerInfo.id.toString())
        )

        coEvery { mockPlayNewAnalytic.clickShop(any(), any(), any()) } returns Unit

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickPartnerNameUpcomingAction)
            }

            /** Verify **/
            verify { mockPlayNewAnalytic.clickShop(any(), any(), any()) }

            events.assertNotEmpty()
            events.last().isEqualTo(mockEvent)
        }
    }

    @Test
    fun `given a upcoming channel, when user click partner name and partner is buyer, then it should emit open page event`() {
        /** Mock */
        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            ApplinkConst.PROFILE,
            listOf(mockChannelDataWithBuyerPartner.partnerInfo.id.toString())
        )

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelDataWithBuyerPartner.id, mockChannelDataWithBuyerPartner)
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickPartnerNameUpcomingAction)
            }

            /** Verify **/
            events.assertNotEmpty()
            events.last().isEqualTo(mockEvent)
        }
    }

    /**
     * Follow
     */
    @Test
    fun `given a upcoming channel, when user click follow and user is not logged in yet, then it should emit open page for login`() {
        /** Mock */
        every { mockUserSession.isLoggedIn } returns false

        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            applink = ApplinkConst.LOGIN,
            requestCode = 1
        )

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE,
            repo = mockRepo
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickFollowUpcomingAction)
            }

            /** Verify **/
            events.assertNotEmpty()
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.OpenPageEvent::requestCode)
        }
    }

    @Test
    fun `given a upcoming channel, when user click follow and user is logged in and partner is not followed yet, then it should follow the user`() {
        /** Mock */
        every { mockUserSession.isLoggedIn } returns true
        coEvery { mockRepo.getIsFollowingPartner(any()) } returns false
        coEvery { mockRepo.postFollowStatus(any(), any()) } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE,
            repo = mockRepo
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val state = robot.recordState {
                robot.submitAction(ClickFollowUpcomingAction)
            }

            /** Verify **/
            state.partner.followStatus.isEqualTo(PlayPartnerFollowStatus.Followable(true))
        }
    }

    @Test
    fun `given a upcoming channel, when partner is not shop, then the partner state should be not followable`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE,
            repo = mockRepo
        )

        robot.use {
            /** Test */
            val state = robot.recordState {
                viewModel.initPage(mockChannelDataWithBuyerPartner.id, mockChannelDataWithBuyerPartner)
            }

            /** Verify **/
            state.partner.followStatus.isEqualTo(PlayPartnerFollowStatus.NotFollowable)
        }
    }

    /**
     * Refresh
     */
    @Test
    fun `given a upcoming channel, when timer finish, then it will wait for waiting duration and show refresh button`() {
        /** Mock */
        val mockStatusResponse = ChannelStatusResponse(
            playGetChannelsStatus = ChannelStatusResponse.Data(
                data = listOf(
                    ChannelStatusResponse.ChannelStatus(
                        channelId = mockChannelData.id,
                        status = "FREEZE"
                    )
                ),
                waitingDuration = 1000
            )
        )

        val mockEvent = PlayUpcomingUiEvent.ShowInfoEvent(UiString.Resource(1))

        coEvery { mockGetChannelStatus.executeOnBackground() } returns mockStatusResponse

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            getChannelStatusUseCase = mockGetChannelStatus,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(UpcomingTimerFinish)
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.Refresh)

            events.assertNotEmpty()
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.ShowInfoEvent::message)
        }
    }

    @Test
    fun `given a upcoming channel, when user click refresh and the channel already active, then upcoming state should be watch now`() {
        /** Mock */
        val mockStatusResponse = ChannelStatusResponse(
            playGetChannelsStatus = ChannelStatusResponse.Data(
                data = listOf(
                    ChannelStatusResponse.ChannelStatus(
                        channelId = mockChannelData.id,
                        status = "ACTIVE"
                    )
                ),
                waitingDuration = 1000
            )
        )

        coEvery { mockGetChannelStatus.executeOnBackground() } returns mockStatusResponse
        coEvery { mockPlayUiModelMapper.mapStatus(any()).isActive } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            getChannelStatusUseCase = mockGetChannelStatus,
            playUiModelMapper = mockPlayUiModelMapper,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val state = robot.recordState {
                robot.submitAction(UpcomingTimerFinish)
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.WatchNow)
        }
    }

    @Test
    fun `given a upcoming channel, when user click refresh and get gql error, then upcoming state should be back to refresh again`() {
        /** Mock */
        coEvery { mockGetChannelStatus.executeOnBackground() } throws Exception("Network Error")

        val mockEvent = PlayUpcomingUiEvent.ShowInfoWithActionEvent(UiString.Resource(1)) {}

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            getChannelStatusUseCase = mockGetChannelStatus,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(UpcomingTimerFinish)
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.Refresh)

            events.assertNotEmpty()
            events.last().isEqualToIgnoringFields(
                mockEvent,
                PlayUpcomingUiEvent.ShowInfoWithActionEvent::message,
                PlayUpcomingUiEvent.ShowInfoWithActionEvent::action
            )
        }
    }

    /**
     * SSE
     */
    @Test
    fun `given a upcoming channel, when channel get message that the channel is already alive, then it should update the channel upcoming state`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
            viewModel.startSSE(mockChannelData.id)
        }

        robot.use {
            /** Test */
            val state = robot.recordState {
                fakePlayChannelSSE.fakeSendMessage("upcommingchannelupdatelive", "{ \"channel_id\" : ${mockChannelData.id} }")
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.WatchNow)
            fakePlayChannelSSE.isConnectionOpen().assertFalse()
        }
    }

    @Test
    fun `given a upcoming channel, when channel get message that the channel is already active, then it should update the channel upcoming state`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
            viewModel.startSSE(mockChannelData.id)
        }

        robot.use {
            /** Test */
            val state = robot.recordState {
                fakePlayChannelSSE.fakeSendMessage("upcommingchannelupdateactive", "{ \"channel_id\" : ${mockChannelData.id} }")
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.WatchNow)
            fakePlayChannelSSE.isConnectionOpen().assertFalse()
        }
    }

    @Test
    fun `given a upcoming channel, when channel get unrecognized event, then it should do nothing`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
            viewModel.startSSE(mockChannelData.id)
        }

        robot.use {
            /** Test */
            val state = robot.recordState {
                fakePlayChannelSSE.fakeSendMessage("unrecognizedevent", "{ \"channel_id\" : ${mockChannelData.id} }")
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.RemindMe)
            fakePlayChannelSSE.isConnectionOpen().assertTrue()
        }
    }

    @Test
    fun `given a upcoming channel, when channel get different channel_id alive, then it should do nothing with current channel`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playAnalytic = mockPlayNewAnalytic,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
            viewModel.startSSE(mockChannelData.id)
        }

        robot.use {
            /** Test */
            val state = robot.recordState {
                fakePlayChannelSSE.fakeSendMessage("upcommingchannelupdatelive", "{ \"channel_id\" : 123123 }")
            }

            /** Verify **/
            state.upcomingInfo.state.isEqualTo(PlayUpcomingState.RemindMe)
            fakePlayChannelSSE.isConnectionOpen().assertTrue()
        }
    }
}