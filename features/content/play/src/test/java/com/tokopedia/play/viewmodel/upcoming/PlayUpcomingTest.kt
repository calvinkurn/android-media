package com.tokopedia.play.viewmodel.upcoming

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.play.data.PlayReminder
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.PlayChannelReminderUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.fake.FakePlayChannelSSE
import com.tokopedia.play.fake.FakePlayShareExperience
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayPartnerInfoModelBuilder
import com.tokopedia.play.model.PlayUpcomingInfoModelBuilder
import com.tokopedia.play.robot.upcoming.createPlayUpcomingViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertNotEmpty
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.util.assertType
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.isEqualToIgnoringFields
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.view.uimodel.action.ClickFollowUpcomingAction
import com.tokopedia.play.view.uimodel.action.ClickPartnerNameUpcomingAction
import com.tokopedia.play.view.uimodel.action.ClickShareUpcomingAction
import com.tokopedia.play.view.uimodel.action.ClickSharingOptionUpcomingAction
import com.tokopedia.play.view.uimodel.action.ClickUpcomingButton
import com.tokopedia.play.view.uimodel.action.ExpandDescriptionUpcomingAction
import com.tokopedia.play.view.uimodel.action.ScreenshotTakenUpcomingAction
import com.tokopedia.play.view.uimodel.action.TapCover
import com.tokopedia.play.view.uimodel.action.UpcomingTimerFinish
import com.tokopedia.play.view.uimodel.event.PlayUpcomingUiEvent
import com.tokopedia.play.view.uimodel.event.ShowInfoEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.PartnerFollowableStatus
import com.tokopedia.play.view.uimodel.recom.PlayPartnerFollowStatus
import com.tokopedia.play.view.uimodel.state.PlayUpcomingState
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
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
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()

    private val mockUpcomingInfo = upcomingInfoModelBuilder.buildUpcomingInfo(isUpcoming = true)
    private val mockPartnerInfo = partnerInfoBuilder.buildPlayPartnerInfo()
    private val mockChannelData = channelDataBuilder.buildChannelData(
        partnerInfo = mockPartnerInfo,
        upcomingInfo = mockUpcomingInfo
    )
    private val mockChannelDataWithBuyerPartner = channelDataBuilder.buildChannelData(
        upcomingInfo = mockUpcomingInfo,
        partnerInfo = PlayPartnerInfoModelBuilder().buildPlayPartnerInfo(
            type = PartnerType.Buyer
        )
    )

    private val channelId = mockChannelData.id
    private val channelInfo = mockChannelData.channelDetail.channelInfo
    private val shareInfo = mockChannelData.channelDetail.shareInfo

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockGetChannelStatus: GetChannelStatusUseCase = mockk(relaxed = true)
    private val mockPlayUiModelMapper: PlayUiModelMapper = mockk(relaxed = true)
    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)
    private val mockPlayShareExperience: PlayShareExperience = mockk(relaxed = true)

    private val fakePlayChannelSSE = FakePlayChannelSSE(testDispatcher)
    private val fakePlayShareExperience = FakePlayShareExperience()
    private val mockChannelDataReminded = mockChannelData.copy(upcomingInfo = mockUpcomingInfo.copy(isReminderSet = true))

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
        every { mockUserSession.isLoggedIn } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test **/
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            state.upcomingInfo.state.assertType<PlayUpcomingState.ReminderStatus> { it.isReminded.assertTrue() }
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
        every { mockUserSession.isLoggedIn } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test **/
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            state.upcomingInfo.state.assertType<PlayUpcomingState.ReminderStatus> { it.isReminded.assertFalse() }
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
            userSession = mockUserSession
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test **/
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            /** Verify */
            state.upcomingInfo.state.assertEqualTo(PlayUpcomingState.Loading)
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.OpenPageEvent::requestCode)
        }
    }

    @Test
    fun `given a upcoming channel, when logged in user already set reminder click cancel remind me button, then upcoming state should be updated to remind me`() {
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
        every { mockUserSession.isLoggedIn } returns true

        val robot = createPlayUpcomingViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelDataReminded)
            print(mockChannelDataReminded.upcomingInfo)
        }

        robot.use {
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            state.upcomingInfo.state.assertType<PlayUpcomingState.ReminderStatus> { it.isReminded.assertFalse() }
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.RemindMeEvent::message)
        }
    }

    @Test
    fun `given a upcoming channel, when logged in user already set reminder click cancel click remind me button and error occur, then upcoming state is still the same`() {
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
        every { mockUserSession.isLoggedIn } returns true

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            playChannelReminderUseCase = mockPlayChannelReminderUseCase,
            dispatchers = testDispatcher,
            userSession = mockUserSession
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelDataReminded)
        }

        robot.use {
            val (state, events) = robot.recordStateAndEvent {
                robot.submitAction(ClickUpcomingButton)
            }

            state.upcomingInfo.state.assertType<PlayUpcomingState.ReminderStatus> { it.isReminded.assertTrue() }
            events.last().isEqualToIgnoringFields(mockEvent, PlayUpcomingUiEvent.RemindMeEvent::message)
        }
    }

    /**
     * Watch Now
     */
    @Test
    fun `given a upcoming channel, when channel already live and user click button, then app should send analytic and close SSE`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
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

            events.assertNotEmpty()
            events.last().assertEqualTo(PlayUpcomingUiEvent.RefreshChannelEvent)
        }
    }

    /**
     * Click Partner Name
     */
    @Test
    fun `given a upcoming channel, when user click partner name should emit open page event`() {
        /** Mock */
        val mockApplink = "tokopedia://shop/1111"
        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            mockApplink
        )

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickPartnerNameUpcomingAction(appLink = mockApplink))
            }

            events.assertNotEmpty()
            events.last().assertEqualTo(mockEvent)
        }
    }

    @Test
    fun `given a upcoming channel, when user click partner name and partner is buyer, then it should emit open page event`() {
        /** Mock */
        val mockApplink = "tokopedia://play/1266"
        val mockEvent = PlayUpcomingUiEvent.OpenPageEvent(
            mockApplink
        )

        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playChannelSSE = fakePlayChannelSSE
        ) {
            viewModel.initPage(mockChannelDataWithBuyerPartner.id, mockChannelDataWithBuyerPartner)
        }

        robot.use {
            /** Test */
            val events = robot.recordEvent {
                robot.submitAction(ClickPartnerNameUpcomingAction(appLink = mockApplink))
            }

            /** Verify **/
            events.assertNotEmpty()
            events.last().assertEqualTo(mockEvent)
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
            state.partner.status.assertEqualTo(PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.Followed))
        }
    }

    @Test
    fun `given a upcoming channel, when partner is buyer, then the partner state should be followable`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            playChannelSSE = fakePlayChannelSSE,
            repo = mockRepo
        )

        robot.use {
            /** Test */
            val state = robot.recordState {
                viewModel.initPage(mockChannelDataWithBuyerPartner.id, mockChannelDataWithBuyerPartner)
            }

            /** Verify **/
            state.partner.status.assertEqualTo(PlayPartnerFollowStatus.Followable(PartnerFollowableStatus.NotFollowed))
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
            state.upcomingInfo.state.assertEqualTo(PlayUpcomingState.Refresh)

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
            state.upcomingInfo.state.assertEqualTo(PlayUpcomingState.WatchNow)
            fakePlayChannelSSE.isConnectionOpen().assertFalse()
        }
    }

    @Test
    fun `given a upcoming channel, when channel get message that the channel is already active, then it should update the channel upcoming state`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
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
            state.upcomingInfo.state.assertEqualTo(PlayUpcomingState.WatchNow)
            fakePlayChannelSSE.isConnectionOpen().assertFalse()
        }
    }

    @Test
    fun `given a upcoming channel, when channel get unrecognized event, then it should do nothing`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
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
            state.upcomingInfo.state.assertType<PlayUpcomingState.ReminderStatus> { it.isReminded.assertFalse() }
            fakePlayChannelSSE.isConnectionOpen().assertTrue()
        }
    }

    @Test
    fun `given a upcoming channel, when channel get different channel_id alive, then it should do nothing with current channel`() {
        /** Prepare */
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            userSession = mockUserSession,
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
            state.upcomingInfo.state.assertType<PlayUpcomingState.ReminderStatus> { it.isReminded.assertFalse() }
            fakePlayChannelSSE.isConnectionOpen().assertTrue()
        }
    }

    /**
     * Share Experience
     */
    @Test
    fun `when user click share action, it should emit event to open universal sharing bottom sheet`() {
        /** Prepare */
        coEvery { mockPlayShareExperience.isCustomSharingAllow() } returns true

        val mockEvent = PlayUpcomingUiEvent.OpenSharingOptionEvent(
            title = channelInfo.title,
            coverUrl = channelInfo.coverUrl,
            userId = "",
            channelId = channelId
        )

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = mockPlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickShareUpcomingAction)
            }

            event.last().assertEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user wants to open sharing experience & custom sharing is allowed, it should emit event to open universal sharing bottom sheet`() {
        /** Prepare */
        coEvery { mockPlayShareExperience.isCustomSharingAllow() } returns true

        val mockEvent = PlayUpcomingUiEvent.OpenSharingOptionEvent(
            title = channelInfo.title,
            coverUrl = channelInfo.coverUrl,
            userId = "",
            channelId = channelId
        )

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = mockPlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickShareUpcomingAction)
            }

            event.last().assertEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user wants to open sharing experience & custom sharing is not allowed, it should emit event to copy the link`() {
        /** Prepare */
        coEvery { mockPlayShareExperience.isCustomSharingAllow() } returns false

        val mockCopyEvent = PlayUpcomingUiEvent.CopyToClipboardEvent(
            shareInfo.content
        )
        val mockShowInfoEvent = PlayUpcomingUiEvent.ShowInfoEvent(
            UiString.Resource(123)
        )

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = mockPlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickShareUpcomingAction)
            }

            /** Verify */
            event[0].assertEqualTo(mockCopyEvent)
            event[1].isEqualToIgnoringFields(mockShowInfoEvent, ShowInfoEvent::message)
        }
    }

    @Test
    fun `when user close sharing bottom sheet, it should send analytics close bottom sheet`() {
        createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = mockPlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }
    }

    @Test
    fun `when user take screenshot & custom share is allowed, it should emit event to open bottom sheet`() {
        /** Prepare */
        coEvery { mockPlayShareExperience.isCustomSharingAllow() } returns true

        val mockEvent = PlayUpcomingUiEvent.OpenSharingOptionEvent(
            title = channelInfo.title,
            coverUrl = channelInfo.coverUrl,
            userId = "",
            channelId = channelId
        )

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = mockPlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ScreenshotTakenUpcomingAction)
            }

            event.last().assertEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user click share option, it should emit event to redirect to selected media`() {
        /** Prepare */
        fakePlayShareExperience.setScreenshotBottomSheet(false)

        val shareModel = ShareModel.Whatsapp()

        val mockCloseBottomSheet = PlayUpcomingUiEvent.CloseShareExperienceBottomSheet

        val mockEvent = PlayUpcomingUiEvent.OpenSelectedSharingOptionEvent(
            linkerShareResult = null,
            shareModel = shareModel,
            ""
        )

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = fakePlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickSharingOptionUpcomingAction(shareModel))
            }

            event[0].assertEqualTo(mockCloseBottomSheet)
            event[1].assertEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user click share option and error occur, it should emit event to copy link`() {
        /** Prepare */
        fakePlayShareExperience.setScreenshotBottomSheet(false)
        fakePlayShareExperience.setThrowException(true)

        val shareModel = ShareModel.Whatsapp()

        val mockCloseBottomSheet = PlayUpcomingUiEvent.CloseShareExperienceBottomSheet

        val mockErrorGenerateLink = PlayUpcomingUiEvent.ErrorGenerateShareLink

        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher,
            playShareExperience = fakePlayShareExperience
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickSharingOptionUpcomingAction(shareModel))
            }

            event[0].assertEqualTo(mockCloseBottomSheet)
            event[1].assertEqualTo(mockErrorGenerateLink)
        }
    }

    /**
     * Expanded
     */
    @Test
    fun `given initial value of desc isExpand is false, when user click Lihat Semua it will set true`() {
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ExpandDescriptionUpcomingAction)
            }

            state.description.isExpand.assertTrue()
            state.description.isShown.assertTrue()
        }
    }

    @Test
    fun `given initial value of desc isExpand is false, when user click Lihat Semua it will set true then user click Tampilkan Sedikit it would go back as initial value`() {
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            // 1 click to expand
            val state1 = robot.recordState {
                robot.submitAction(ExpandDescriptionUpcomingAction)
            }

            // 2 click to dismiss
            val state2 = robot.recordState {
                robot.submitAction(ExpandDescriptionUpcomingAction)
            }

            state1.description.isExpand.assertTrue()
            state2.description.isExpand.assertFalse()
            state2.description.isShown.assertTrue()
        }
    }

    @Test
    fun `given initial value of desc isExpand is false, when user tap cover the isShown must be false`() {
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            val state = robot.recordState {
                robot.submitAction(TapCover)
            }

            state.description.isShown.assertFalse()
            state.description.isExpand.assertFalse()
        }
    }

    @Test
    fun `expand description isexpanded tap cover, isExpand value must be false, and isShown is true`() {
        val robot = createPlayUpcomingViewModelRobot(
            dispatchers = testDispatcher
        ) {
            viewModel.initPage(mockChannelData.id, mockChannelData)
        }

        robot.use {
            val state = robot.recordState {
                robot.submitAction(ExpandDescriptionUpcomingAction)
                robot.submitAction(TapCover)
            }
            state.description.isShown.assertTrue()
            state.description.isExpand.assertFalse()
        }
    }
}
