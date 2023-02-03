package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.data.interactive.PostInteractiveTapResponse
import com.tokopedia.play.data.repository.PlayViewerInteractiveRepositoryImpl
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play.domain.interactive.PostInteractiveTapUseCase
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlaySocketResponseBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.repo.PlayViewerMockRepository
import com.tokopedia.play.robot.andThen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.util.millisFromNow
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.domain.model.interactive.GetCurrentInteractiveResponse
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.model.dto.interactive.GameUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 15/07/21
 */
class PlayTapInteractiveTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = CoroutineTestDispatchers

    private val interactiveId = "abc"

    private val socketResponseBuilder = PlaySocketResponseBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val videoBuilder = PlayVideoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live),
        ),
        videoMetaInfo = videoBuilder.buildVideoMeta(
            videoPlayer = videoBuilder.buildCompleteGeneralVideoPlayer(),
        )
    )

    private val socketFlow = MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                    socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
            )
    )
    private val socket: PlayWebSocket = mockk(relaxed = true)
    private val mockMapper: PlayUiModelMapper = mockk(relaxed = true)
    private val mockInteractiveStorage = object : PlayInteractiveStorage {
        private var hasJoined = false

        override fun setDetail(interactiveId: String, model: PlayCurrentInteractiveModel) {

        }

        override fun setActive(interactiveId: String) {

        }

        override fun setHasProcessedWinner(interactiveId: String) {

        }

        override fun save(model: GameUiModel) {

        }

        override fun hasProcessedWinner(interactiveId: String): Boolean {
            return false
        }

        override fun setJoined(interactiveId: String) {
            hasJoined = true
        }

        override fun getDetail(interactiveId: String): PlayCurrentInteractiveModel? {
            return null
        }

        override fun getActiveInteractiveId(): String {
            return interactiveId
        }

        override fun hasJoined(interactiveId: String): Boolean {
            return hasJoined
        }
    }
    private val mockPostInteractiveTapUseCase: PostInteractiveTapUseCase = mockk(relaxed = true)
    private val mockCurrentInteractiveUseCase: GetCurrentInteractiveUseCase = mockk(relaxed = true)

    private val interactiveRepo: PlayViewerInteractiveRepository =
        PlayViewerInteractiveRepositoryImpl(
            getCurrentInteractiveUseCase = mockCurrentInteractiveUseCase,
            postInteractiveTapUseCase = mockPostInteractiveTapUseCase,
            getInteractiveViewerLeaderboardUseCase = mockk(relaxed = true),
            answerQuizUseCase = mockk(relaxed = true),
            mapper = mockMapper,
            dispatchers = testDispatcher,
            interactiveStorage = mockInteractiveStorage
        )

    private val repo = PlayViewerMockRepository.get(interactiveRepo = interactiveRepo)

    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    init {
        coEvery { mockCurrentInteractiveUseCase.executeOnBackground() } returns GetCurrentInteractiveResponse()
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
        every { socket.listenAsFlow() } returns socketFlow
    }

    @Test
    fun `given tap is active and tap is success, when click tap, then user should have joined the tap`() {
        val durationTap = 5000L.millisFromNow()
        val title = "Giveaway"
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Giveaway(
            id = "1",
            title = title,
            waitingDuration = 200L,
            status = GameUiModel.Giveaway.Status.Ongoing(
                endTime = durationTap,
            )
        )

        coEvery { mockPostInteractiveTapUseCase.executeOnBackground() } returns PostInteractiveTapResponse()

        givenPlayViewModelRobot(
            playChannelWebSocket = socket,
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            mockInteractiveStorage.hasJoined(interactiveId)
                    .assertFalse()
        }.andThen {
            viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
        }.thenVerify {
            mockInteractiveStorage.hasJoined(interactiveId)
                    .assertTrue()
        }
    }

    @Test
    fun `given tap is active and tap is error, when click tap, then user should have not joined the tap`() {
        val durationTap = 5000L.millisFromNow()
        val title = "Giveaway"
        coEvery { mockMapper.mapInteractive(any<GetCurrentInteractiveResponse.Data>()) } returns GameUiModel.Giveaway(
            id = "1",
            title = title,
            waitingDuration = 200L,
            status = GameUiModel.Giveaway.Status.Ongoing(
                endTime = durationTap,
            )
        )
        coEvery { mockPostInteractiveTapUseCase.executeOnBackground() } throws MessageErrorException("")

        givenPlayViewModelRobot(
                playChannelWebSocket = socket,
                repo = repo,
                dispatchers = testDispatcher
        ) {
            createPage(mockChannelData)
            focusPage(mockChannelData)
        }.thenVerify {
            mockInteractiveStorage.hasJoined(interactiveId)
                    .assertFalse()
        }.andThen {
            viewModel.submitAction(PlayViewerNewAction.TapGiveaway)
        }.thenVerify {
            mockInteractiveStorage.hasJoined(interactiveId)
                    .assertFalse()
        }
    }
}
