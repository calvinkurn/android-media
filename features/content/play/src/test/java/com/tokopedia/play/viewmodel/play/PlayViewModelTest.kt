package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.CommentVisibilityAction
import com.tokopedia.play.view.uimodel.action.DismissFollowPopUp
import com.tokopedia.play.view.uimodel.action.OpenCart
import com.tokopedia.play.view.uimodel.event.CommentVisibilityEvent
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataModelBuilder = PlayChannelDataModelBuilder()

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()

    private val testDispatcher = CoroutineTestDispatchers

    private val vodChannelData = channelDataModelBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.VOD
            ),
            commentUiModel = channelInfoBuilder.buildCommentConfig(shouldShow = true, count = "125,5 rb")
        )
    )

    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given video player instance is created, when retrieved, it should return the correct video player instance`() {
        val mockPlayerBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true)
        val mockPlayer: PlayVideoWrapper = mockk(relaxed = true)
        every { mockPlayerBuilder.build() } returns mockPlayer

        givenPlayViewModelRobot(
                playVideoBuilder = mockPlayerBuilder
        ) andWhen {
            getVideoPlayer()
        } thenVerify { result ->
            result.isEqualTo(mockPlayer)
        }
    }

    @Test
    fun `given channel data is set, when retrieved, it should return same data`() {
        val channelData = channelDataModelBuilder.buildChannelData()

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData

        givenPlayViewModelRobot(
            repo = repo
        ) {
            createPage(channelData)
        } thenVerify {
            viewModel.latestCompleteChannelData.assertEqualTo(channelData)
        }
    }

    @Test
    fun `when get new product, track product should be called`() {
        /**
         * TODO () = if tracker is on update
         *
         *
        val trackProductUseCase: TrackProductTagBroadcasterUseCase = mockk(relaxed = true)
        val mockSocket: PlayWebSocket = mockk(relaxed = true)
        val socketFlow = MutableStateFlow<WebSocketAction?>(null)

        var isCalled = false

        val channelData = channelDataModelBuilder.buildChannelData()

        every { mockSocket.listenAsFlow() } returns socketFlow.filterNotNull()

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData
        coEvery { repo.trackProducts(any(), any()) } answers {
            isCalled = true
        }

        givenPlayViewModelRobot(
            repo = repo,
            playChannelWebSocket = mockSocket,
            dispatchers = testDispatcher,
            playSocketToModelMapper = mapperBuilder.buildSocketMapper(),
        ) {
            createPage(channelData)
            focusPage(channelData)
        } thenVerify {
            isCalled.assertFalse()
        } andThen {
            runTest(testDispatcher.coroutineDispatcher) {
                socketFlow.emit(
                        WebSocketAction.NewMessage(socketResponseBuilder.buildProductTagResponse())
                )
            }
        } thenVerify {
            isCalled.assertTrue()
        }
        */
    }

    @Test
    fun `action to open comment, fetch gql, update event`() {
        val robot = createPlayViewModelRobot(
            repo = mockRepo
        )

        coEvery { mockRepo.getCountComment(any()) } returns vodChannelData.channelDetail.commentConfig

        robot.use {
            it.createPage(vodChannelData)
            it.focusPage(vodChannelData)
            val stateAndEvent = it.recordStateAndEvent {
                viewModel.submitAction(CommentVisibilityAction(isOpen = true))
            }
            stateAndEvent.first.channel.commentConfig.assertEqualTo(vodChannelData.channelDetail.commentConfig)
            stateAndEvent.second.last().assertEqualTo(CommentVisibilityEvent(true))
            it.viewModel.isAnyBottomSheetsShown.assertTrue()
        }

        coVerify { mockRepo.getCountComment(any()) }
    }

    @Test
    fun `action to hide comment, dont fetch gql, update event`() {
        val robot = createPlayViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            it.createPage(vodChannelData)
            it.focusPage(vodChannelData)

            val stateAndEvent = it.recordStateAndEvent {
                viewModel.submitAction(CommentVisibilityAction(isOpen = false))
            }
            stateAndEvent.second.last().assertEqualTo(CommentVisibilityEvent(false))
            it.viewModel.isAnyBottomSheetsShown.assertFalse()
        }

        coVerify { mockRepo.getCountComment(any()) wasNot called }
    }

    @Test
    fun `open cart - non login`() {
        val robot = createPlayViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            it.createPage(vodChannelData)
            it.focusPage(vodChannelData)

            it.setLoggedIn(false)

            val stateAndEvent = it.recordStateAndEvent {
                viewModel.submitAction(OpenCart)
            }

            stateAndEvent.second.last().assertEqualTo(OpenPageEvent(ApplinkConst.LOGIN, requestCode = 578))
        }
    }

    @Test
    fun `open cart - login`() {
        val robot = createPlayViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            it.createPage(vodChannelData)
            it.focusPage(vodChannelData)

            it.setLoggedIn(true)

            val stateAndEvent = it.recordStateAndEvent {
                viewModel.submitAction(OpenCart)
            }
            stateAndEvent.second.last().assertEqualTo(OpenPageEvent(ApplinkConstInternalMarketplace.CART, pipMode = true))
        }
    }

    @Test
    fun `dismiss follow pop up - not bottomsheet shown`() {
        val robot = createPlayViewModelRobot(
            repo = mockRepo
        )

        robot.use {
            it.createPage(vodChannelData)
            it.focusPage(vodChannelData)

            it.setLoggedIn(true)
            it.viewModel.submitAction(DismissFollowPopUp)
            it.viewModel.isAnyBottomSheetsShown.assertFalse()
        }
    }
}
