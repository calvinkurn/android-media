package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.fake.FakePlayWebSocket
import com.tokopedia.play.fake.chat.FakeChatManager
import com.tokopedia.play.fake.chat.FakeChatStreams
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andThen
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.withState
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.util.chat.ChatManager
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play.view.uimodel.event.ShowCoachMarkWinnerEvent
import com.tokopedia.play.view.uimodel.event.ShowWinningDialogEvent
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.websocket.response.*
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.view.game.quiz.PlayQuizOptionState
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on September 14, 2021
 */
class PlayViewModelWebSocketTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    private val videoInfoBuilder = PlayVideoModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()
    private val channelDataModelBuilder = PlayChannelDataModelBuilder()
    private val channelData = channelDataModelBuilder.buildChannelData(
        videoMetaInfo = videoInfoBuilder.buildVideoMeta(
            videoPlayer = videoModelBuilder.buildCompleteGeneralVideoPlayer()
        )
    )
    private val mapperBuilder = PlayMapperBuilder()
    private lateinit var fakePlayWebSocket: FakePlayWebSocket
    private val repo: PlayViewerRepository = mockk(relaxed = true)
    private val mockRemoteConfig: RemoteConfig = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val testDispatcher = CoroutineTestDispatchers

    private val modelBuilder = UiModelBuilder.get()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        fakePlayWebSocket = FakePlayWebSocket(testDispatcher)

        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
        every { repo.getChannelData(any()) } returns channelData
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when get total like data from web socket, then it should update the data`() {
        givenPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayTotalLikeSocketResponse.response)
        } thenVerify {
            withState {
                like.totalLike.isEqualTo(PlayTotalLikeSocketResponse.totalLikeFormatted)
            }
        }
    }

    @Test
    fun `when get total view data from web socket, then it should update the data`() {
        givenPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayTotalViewSocketResponse.response)
        } thenVerify {
            withState {
                totalView.viewCountStr
                    .isEqualTo(PlayTotalViewSocketResponse.totalViewFormatted)
            }
        }
    }

    @Test
    fun `when get multiple new chat from web socket, then the latest chat should be emitted`() {
        val message1 = "Message 1"
        val message2 = "Message 2"

        val mockChatStreams = FakeChatStreams(
            CoroutineScope(testDispatcher.main),
            dispatchers = testDispatcher
        )
        val mockChatManager = FakeChatManager(mockChatStreams)

        val chatStreamsFactory = object : ChatStreams.Factory {
            override fun create(scope: CoroutineScope): ChatStreams {
                return mockChatStreams
            }
        }
        val chatManagerFactory = object : ChatManager.Factory {
            override fun create(chatStreams: ChatStreams): ChatManager {
                return mockChatManager
            }
        }

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            dispatchers = testDispatcher,
            chatManagerFactory = chatManagerFactory,
            chatStreamsFactory = chatStreamsFactory,
        )

        robot.use {
            it.createPage(channelData)
            it.focusPage(channelData)

            fakePlayWebSocket.fakeReceivedMessage(PlayChatSocketResponse.generateResponse(message1))
            fakePlayWebSocket.fakeReceivedMessage(PlayChatSocketResponse.generateResponse(message2))
            testDispatcher.coroutineDispatcher.advanceTimeBy(500)
        }

        robot.viewModel.chats.value.last().message.assertEqualTo(message2)
    }

    @Test
    fun `when get pinned message data from web socket, then it should update the data`() {
        val pinnedMessageModelBuilder = PlayPinnedModelBuilder()

        val pinnedMessageObserver: Observer<PinnedMessageUiModel> = mockk(relaxed = true)
        every { pinnedMessageObserver.onChanged(any()) }.just(Runs)

        val mockResponse = pinnedMessageModelBuilder.buildPinnedMessage(
            id = PlayPinnedMessageSocketResponse.pinnedMessageId.toString(),
            appLink = PlayPinnedMessageSocketResponse.redirectUrl,
            title = PlayPinnedMessageSocketResponse.title
        )

        givenPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            viewModel.observablePinnedMessage.observeForever(pinnedMessageObserver)

            createPage(channelData)
            focusPage(channelData)
        } andThen {
            fakePlayWebSocket.fakeReceivedMessage(PlayPinnedMessageSocketResponse.response)
        } thenVerify {
            verifySequence {
                pinnedMessageObserver.onChanged(channelData.pinnedInfo.pinnedMessage)
                pinnedMessageObserver.onChanged(mockResponse)
            }
        }
    }

//    @Test
//    fun `when get quick reply data from web socket, then it should update the data`() {
//        val quickReplyModelBuilder = PlayQuickReplyModelBuilder()
//        every { mockUserSession.userId } returns "1"
//        val quickReplyObserver: Observer<PlayQuickReplyInfoUiModel> = mockk(relaxed = true)
//        every { quickReplyObserver.onChanged(any()) }.just(Runs)
//
//        val mockResponse = quickReplyModelBuilder.build(
//            quickReplyList = PlayQuickReplySocketResponse.quickReplyList
//        )
//
//        givenPlayViewModelRobot(
//            playChannelWebSocket = playChannelWebSocket,
//            dispatchers = testDispatcher,
//            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
//        ) {
//            viewModel.observableQuickReply.observeForever(quickReplyObserver)
//
//            createPage(channelData)
//            focusPage(channelData)
//        } andThen {
//            fakePlayWebSocket.fakeReceivedMessage(PlayQuickReplySocketResponse.response)
//        } thenVerify {
//            verifySequence {
//                quickReplyObserver.onChanged(channelData.quickReplyInfo)
//                quickReplyObserver.onChanged(mockResponse)
//            }
//        }
//    }

//    @Test
//    fun `when get banned status from web socket, then it should update the data`() {
//        val statusInfoObserver: Observer<PlayStatusUiModel> = mockk(relaxed = true)
//        every { statusInfoObserver.onChanged(any()) }.just(Runs)
//
//        every { mockUserSession.userId } returns "1"
//
//        val mockResponse = statusInfoModelBuilder.build(
//            statusType = PlayStatusType.Banned
//        )
//
//        givenPlayViewModelRobot(
//            playChannelWebSocket = playChannelWebSocket,
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
//        ) {
//            viewModel.observableStatusInfo.observeForever(statusInfoObserver)
//
//            createPage(channelData)
//            focusPage(channelData)
//        } andThen {
//            fakePlayWebSocket.fakeReceivedMessage(PlayBannedSocketResponse.generateResponse())
//        } thenVerify {
//            verifySequence {
//                statusInfoObserver.onChanged(channelData.statusInfo)
//                statusInfoObserver.onChanged(mockResponse)
//            }
//        }
//    }

//    @Test
//    fun `when get banned status from web socket with different channelId than current channelId, then no new data should be emitted`() {
//        val statusInfoObserver: Observer<PlayStatusInfoUiModel> = mockk(relaxed = true)
//        every { statusInfoObserver.onChanged(any()) }.just(Runs)
//
//        every { mockUserSession.userId } returns "1"
//
//        givenPlayViewModelRobot(
//            playChannelWebSocket = playChannelWebSocket,
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
//        ) {
//            viewModel.observableStatusInfo.observeForever(statusInfoObserver)
//
//            createPage(channelData)
//            focusPage(channelData)
//        } andThen {
//            fakePlayWebSocket.fakeReceivedMessage(PlayBannedSocketResponse.generateResponse(channelId = "2"))
//        } thenVerify {
//            verifySequence {
//                statusInfoObserver.onChanged(channelData.statusInfo)
//            }
//        }
//    }

//    @Test
//    fun `when get banned status from web socket with different userId than current userId, then it should not update the data to banned`() {
//        val statusInfoObserver: Observer<PlayStatusInfoUiModel> = mockk(relaxed = true)
//        every { statusInfoObserver.onChanged(any()) }.just(Runs)
//
//        every { mockUserSession.userId } returns "1"
//
//        givenPlayViewModelRobot(
//            playChannelWebSocket = playChannelWebSocket,
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
//        ) {
//            viewModel.observableStatusInfo.observeForever(statusInfoObserver)
//
//            createPage(channelData)
//            focusPage(channelData)
//        } andThen {
//            fakePlayWebSocket.fakeReceivedMessage(PlayBannedSocketResponse.generateResponse(userId = "2"))
//        } thenVerify {
//            verifySequence {
//                statusInfoObserver.onChanged(channelData.statusInfo)
//                statusInfoObserver.onChanged(channelData.statusInfo)
//            }
//        }
//    }

//    @Test
//    fun `when get product tag from web socket, then it should update the current pinned product`() {
//        val pinnedModelBuilder = PlayPinnedModelBuilder()
//        val productTagModelBuilder = PlayProductTagsModelBuilder()
//
//        val pinnedProductObserver: Observer<PinnedProductUiModel> = mockk(relaxed = true)
//        every { pinnedProductObserver.onChanged(any()) }.just(Runs)
//
//        val mockName = "Product Name Test"
//        val mockSize = 3
//
//        val mockProduct = productTagModelBuilder.buildCompleteData(
//            productList = List(mockSize) { productTagModelBuilder.buildProductLine(title = mockName) },
//        )
//        val mockData = pinnedModelBuilder.buildPinnedProduct(
//            productTags = mockProduct
//        )
//
//        givenPlayViewModelRobot(
//            playChannelWebSocket = playChannelWebSocket,
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
//        ) {
//            viewModel.observablePinnedProduct.observeForever(pinnedProductObserver)
//
//            createPage(channelData)
//            focusPage(channelData)
//        } andThen {
//            fakePlayWebSocket.fakeReceivedMessage(PlayProductTagSocketResponse.generateResponse(size = 3, title = mockName))
//        } thenVerify {
//            when(val productTag = viewModel.observablePinnedProduct.value?.productTags) {
//                is PlayProductTagsUiModel.Complete -> {
//                    val mockSize = (mockData.productTags as PlayProductTagsUiModel.Complete).productList.size
//                    productTag.productList.size.isEqualTo(mockSize)
//
//                    for(i in 0 until mockSize) {
//                        val counter = i+1
//                        (productTag.productList[i] as PlayProductUiModel.Product).title.isEqualTo("$mockName $counter")
//                        (productTag.productList[i] as PlayProductUiModel.Product).id.isEqualTo("$counter")
//                    }
//                }
//                else -> {
//                    fail(Exception("Model should be PlayProductTagsUiModel.Complete"))
//                }
//            }
//        }
//    }

//    @Test
//    fun `when get merchant voucher from web socket, then it should update the current pinned voucher`() {
//        val pinnedModelBuilder = PlayPinnedModelBuilder()
//        val productTagModelBuilder = PlayProductTagsModelBuilder()
//
//        val pinnedProductObserver: Observer<PinnedProductUiModel> = mockk(relaxed = true)
//        every { pinnedProductObserver.onChanged(any()) }.just(Runs)
//
//        val mockSize = 3
//        val mockTitle = "Diskon Testing"
//        val mockVoucher = productTagModelBuilder.buildCompleteData(
//            voucherList = List(mockSize) {
//                productTagModelBuilder.buildMerchantVoucher(
//                    title = "$mockTitle ${it+1}%"
//                )
//            }
//        )
//        val mockData = pinnedModelBuilder.buildPinnedProduct(
//            productTags = mockVoucher
//        )
//
//        givenPlayViewModelRobot(
//            playChannelWebSocket = playChannelWebSocket,
//            dispatchers = testDispatcher,
//            userSession = mockUserSession,
//            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
//        ) {
//            viewModel.observablePinnedProduct.observeForever(pinnedProductObserver)
//
//            createPage(channelData)
//            focusPage(channelData)
//        } andThen {
//            fakePlayWebSocket.fakeReceivedMessage(PlayMerchantVoucherSocketResponse.generateResponse(size = 3, title = mockTitle))
//        } thenVerify {
//            when(val merchantVoucher = viewModel.observablePinnedProduct.value?.productTags) {
//                is PlayProductTagsUiModel.Complete -> {
//                    val mockMerchantVoucher = (mockData.productTags as PlayProductTagsUiModel.Complete).voucherList
//                    merchantVoucher.voucherList.forEachIndexed { index, playVoucherUiModel ->
//                        (playVoucherUiModel as Merchant).isEqualToIgnoringFields(mockMerchantVoucher[index], Merchant::impressHolder)
//                    }
//                }
//                else -> {
//                    fail(Exception("Model should be PlayProductTagsUiModel.Complete"))
//                }
//            }
//        }
//    }

    @Test
    fun `when get user winner status from socket, then it should show leaderboard data`() {
        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Ongoing(10000L.millisFromNow())
        )

        coEvery { repo.postGiveawayTap(any(), any()) } returns true
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.createPage(channelData)
            it.focusPage(channelData)

            val state = robot.recordState {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
                viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            state.winnerBadge.shouldShow.assertTrue()
        }
    }

    @Test
    fun `when get user winner status from socket, and the current user is win, then it should emit winner popup`() {
        val giveaway = uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Ongoing(10000L.millisFromNow())
        )
        coEvery { repo.getCurrentInteractive(any()) } returns giveaway

        coEvery { repo.postGiveawayTap(any(), any()) } returns true
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
                viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.last().assertEqualTo(
                ShowWinningDialogEvent(
                    PlayUserWinnerStatusSocketResponse.imageUrl,
                    PlayUserWinnerStatusSocketResponse.winnerTitle,
                    PlayUserWinnerStatusSocketResponse.winnerText,
                    GameUiModel.Unknown,
                )
            )
        }
    }

    @Test
    fun `when get user winner status from socket, and the current user is lose, then it should emit loser popup`() {
        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Ongoing(10000L.millisFromNow())
        )

        coEvery { repo.postGiveawayTap(any(), any()) } returns true
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("2")
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
                viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.last().isEqualTo(
                ShowCoachMarkWinnerEvent(
                    PlayUserWinnerStatusSocketResponse.loserTitle,
                    UiString.Text(PlayUserWinnerStatusSocketResponse.loserText),
                )
            )
        }
    }

    @Test
    fun `when the user winner status coming when interactive is not done yet, then it does nothing`() {
        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Ongoing(10000L.millisFromNow())
        )

        coEvery { repo.postGiveawayTap(any(), any()) } returns true
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.isEqualTo(emptyList())
        }
    }

    @Test
    fun `when get user winner status from socket, and the current user is not join the session, then it does nothing`() {
        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Ongoing(10000L.millisFromNow())
        )

        coEvery { repo.postGiveawayTap(any(), any()) } returns true
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns false

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
                viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.isEqualTo(emptyList())
        }
    }

    @Test
    fun `when interactive has ended and still not getting any user winner status from websocket, then it should show leaderboard anyway`() {
        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Ongoing(10000L.millisFromNow())
        )

        coEvery { repo.postGiveawayTap(any(), any()) } returns true
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns false

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.createPage(channelData)
            it.focusPage(channelData)

            val state = robot.recordState {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
                viewModel.submitAction(PlayViewerNewAction.GiveawayOngoingEnded)
            }

            //TODO("Please Check this")
//            state.winnerBadge.shouldShow.assertTrue()
        }
    }

    @Test
    fun `when quiz is finish get user winner status from socket but  user is not join the game, then it does nothing`() {
        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildQuiz(id = "1")

        coEvery { repo.answerQuiz(any(), any()) } returns "2"
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns false

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("11")
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.assertEqualTo(emptyList())
        }
    }

    @Test
    fun `when quiz is ongoing, user join the game whether they a winner or not show coachmark`() {
        val model = uiModelBuilder.buildQuiz(
            id = "1", listOfChoices =
            listOf(
                modelBuilder.buildQuizChoices(
                    id = "12",
                    text = "25 June",
                    type = PlayQuizOptionState.Default('a')
                ),
                modelBuilder.buildQuizChoices(
                    id = "13",
                    text = "26 June",
                    type = PlayQuizOptionState.Default('b')
                ),
                modelBuilder.buildQuizChoices(
                    id = "14",
                    text = "27 June",
                    type = PlayQuizOptionState.Default('c')
                )
            ),
            status = GameUiModel.Quiz.Status.Ongoing(500L.millisFromNow())
        )

        coEvery { repo.getCurrentInteractive(any()) } returns model

        coEvery { repo.answerQuiz(any(), any()) } returns "12"
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.setLoggedIn(true)
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(
                        modelBuilder.buildQuizChoices(
                            id = "12",
                            text = "25 June",
                            type = PlayQuizOptionState.Default('a')
                        )
                    )
                )
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.last().assertInstanceOf<ShowCoachMarkWinnerEvent>()
        }
    }

    @Test
    fun `when quiz is ongoing, user join the game that has a reward, user the loser - wrong answer`() {
        val model = uiModelBuilder.buildQuiz(
            id = "1", listOfChoices =
            listOf(
                modelBuilder.buildQuizChoices(
                    id = "12",
                    text = "25 June",
                    type = PlayQuizOptionState.Default('a')
                ),
                modelBuilder.buildQuizChoices(
                    id = "13",
                    text = "26 June",
                    type = PlayQuizOptionState.Default('b')
                ),
                modelBuilder.buildQuizChoices(
                    id = "14",
                    text = "27 June",
                    type = PlayQuizOptionState.Default('c')
                )
            ),
            status = GameUiModel.Quiz.Status.Ongoing(500L.millisFromNow())
        )

        coEvery { repo.getCurrentInteractive(any()) } returns model

        coEvery { repo.answerQuiz(any(), any()) } returns "12"
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("9")
            it.setLoggedIn(true)
            it.createPage(channelData)
            it.focusPage(channelData)

            val event = robot.recordEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(
                        modelBuilder.buildQuizChoices(
                            id = "14",
                            text = "27 June",
                            type = PlayQuizOptionState.Default('a')
                        )
                    )
                )
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
                fakePlayWebSocket.fakeReceivedMessage(PlayUserWinnerStatusSocketResponse.generateResponse())
            }

            event.last().assertEqualTo(
                ShowCoachMarkWinnerEvent(
                    "",
                    UiString.Resource(R.string.play_quiz_finished),
                )
            )
        }
    }

    @Test
    fun `when quiz is ongoing, user join the game that has no  reward just show leaderboard`() {
        val model = uiModelBuilder.buildQuiz(
            id = "1", listOfChoices =
            listOf(
                modelBuilder.buildQuizChoices(
                    id = "12",
                    text = "25 June",
                    type = PlayQuizOptionState.Default('a')
                ),
                modelBuilder.buildQuizChoices(
                    id = "13",
                    text = "26 June",
                    type = PlayQuizOptionState.Default('b')
                ),
                modelBuilder.buildQuizChoices(
                    id = "14",
                    text = "27 June",
                    type = PlayQuizOptionState.Default('c')
                )
            ),
            status = GameUiModel.Quiz.Status.Ongoing(500L.millisFromNow())
        )

        coEvery { repo.getCurrentInteractive(any()) } returns model

        coEvery { repo.answerQuiz(any(), any()) } returns "12"
        coEvery { repo.getActiveInteractiveId() } returns "1"
        coEvery { repo.hasJoined(any()) } returns true

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("9")
            it.setLoggedIn(true)
            it.createPage(channelData)
            it.focusPage(channelData)

            val eventAndState = robot.recordStateAndEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(
                        modelBuilder.buildQuizChoices(
                            id = "14",
                            text = "27 June",
                            type = PlayQuizOptionState.Default('a')
                        )
                    )
                )
                viewModel.submitAction(PlayViewerNewAction.QuizEnded)
            }
            eventAndState.first.winnerBadge.shouldShow.assertTrue()
        }
    }

    @Test
    fun `when quiz is ongoing, user join the game when click option error occurs, show error toaster and dont change state`() {
        val model = uiModelBuilder.buildQuiz(
            id = "1",
            status = GameUiModel.Quiz.Status.Ongoing(5000L.millisFromNow()),
            listOfChoices =
            listOf(
                modelBuilder.buildQuizChoices(
                    id = "12",
                    text = "25 June",
                    type = PlayQuizOptionState.Default('a')
                ),
                modelBuilder.buildQuizChoices(
                    id = "13",
                    text = "26 June",
                    type = PlayQuizOptionState.Default('b')
                ),
                modelBuilder.buildQuizChoices(
                    id = "14",
                    text = "27 June",
                    type = PlayQuizOptionState.Default('c')
                )
            ),
        )

        val err = MessageErrorException("Error gk bs jawab y")

        coEvery { repo.getCurrentInteractive(any()) } returns model

        coEvery { repo.answerQuiz(any(), any()) } throws err
        coEvery { repo.getActiveInteractiveId() } returns "1"

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("9")
            it.setLoggedIn(true)
            it.createPage(channelData)
            it.focusPage(channelData)

            val eventAndState = robot.recordStateAndEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(
                        modelBuilder.buildQuizChoices(
                            id = "14",
                            text = "27 June",
                            type = PlayQuizOptionState.Default('a')
                        )
                    )
                )
            }
            eventAndState.second.last().assertInstanceOf<ShowErrorEvent>()
        }
    }

    @Test
    fun `when quiz is ongoing, user join the game when click option success, the last event must be answered`() {
        val model = uiModelBuilder.buildQuiz(
            id = "1", listOfChoices =
            listOf(
                modelBuilder.buildQuizChoices(
                    id = "12",
                    text = "25 June",
                    type = PlayQuizOptionState.Default('a')
                ),
                modelBuilder.buildQuizChoices(
                    id = "13",
                    text = "26 June",
                    type = PlayQuizOptionState.Default('b')
                ),
                modelBuilder.buildQuizChoices(
                    id = "14",
                    text = "27 June",
                    type = PlayQuizOptionState.Default('c')
                )
            ),
            status = GameUiModel.Quiz.Status.Ongoing(500L.millisFromNow())
        )

        coEvery { repo.getCurrentInteractive(any()) } returns model

        coEvery { repo.answerQuiz(any(), any()) } returns "12"
        coEvery { repo.getActiveInteractiveId() } returns "1"

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("9")
            it.setLoggedIn(true)
            it.createPage(channelData)
            it.focusPage(channelData)

            val eventAndState = robot.recordStateAndEvent {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(
                    PlayViewerNewAction.ClickQuizOptionAction(
                        modelBuilder.buildQuizChoices(
                            id = "14",
                            text = "27 June",
                            type = PlayQuizOptionState.Default('a')
                        )
                    )
                )
            }
            eventAndState.second.last().assertInstanceOf<QuizAnsweredEvent>()
        }
    }

    @Test
    fun `given giveaway is upcoming when it ends, will change to ongoing`() {
        val durationTap = 5000L.millisFromNow()

        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            id = "1",
            status = GameUiModel.Giveaway.Status.Upcoming(endTime = durationTap, startTime = 100L.millisFromNow())
        )

        val robot = createPlayViewModelRobot(
            playChannelWebSocket = fakePlayWebSocket,
            repo = repo,
            dispatchers = testDispatcher,
            userSession = mockUserSession,
            remoteConfig = mockRemoteConfig,
        )

        robot.use {
            it.setUserId("1")
            it.createPage(channelData)
            it.focusPage(channelData)

            val state = robot.recordState {
                fakePlayWebSocket.fakeReceivedMessage(PlayInteractiveStatusSocketResponse.generateResponse())
                viewModel.submitAction(PlayViewerNewAction.GiveawayUpcomingEnded)
            }

            state.interactive.game.assertInstanceOf<GameUiModel.Giveaway>()
            it.viewModel.gameData.assertInstanceOf<GameUiModel.Giveaway>()
            state.interactive.game.assertType<GameUiModel.Giveaway> { ga ->
                ga.status.assertEqualTo(GameUiModel.Giveaway.Status.Ongoing(endTime = durationTap))
            }
            it.viewModel.gameData.assertType<GameUiModel.Giveaway> { ga ->
                ga.status.assertEqualTo(GameUiModel.Giveaway.Status.Ongoing(endTime = durationTap))
            }
        }
    }
}
