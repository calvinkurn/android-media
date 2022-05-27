package com.tokopedia.play.viewmodel.interactive

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.data.repository.PlayViewerInteractiveRepositoryImpl
import com.tokopedia.play.domain.interactive.AnswerQuizUseCase
import com.tokopedia.play.domain.repository.PlayViewerInteractiveRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlaySocketResponseBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.repo.PlayViewerMockRepository
import com.tokopedia.play.view.storage.interactive.PlayInteractiveStorage
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.model.dto.interactive.PlayCurrentInteractiveModel
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 17/05/22
 */
@ExperimentalCoroutinesApi
class PlayQuizInteractiveTest {
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
        override fun setDetail(interactiveId: String, model: PlayCurrentInteractiveModel) {}
        override fun setActive(interactiveId: String) {}
        override fun setHasProcessedWinner(interactiveId: String) {}
        override fun save(model: InteractiveUiModel) {}
        override fun hasProcessedWinner(interactiveId: String): Boolean = false
        override fun setJoined(id: String) {
            hasJoined = true
        }

        override fun getDetail(interactiveId: String): PlayCurrentInteractiveModel? = null
        override fun getActiveInteractiveId(): String = interactiveId
        override fun hasJoined(id: String): Boolean = hasJoined
    }

    private val mockCurrentInteractiveUseCase: GetCurrentInteractiveUseCase = mockk(relaxed = true)
    private val mockAnswerQUizUseCase: AnswerQuizUseCase = mockk(relaxed = true)

    private val interactiveRepo: PlayViewerInteractiveRepository =
        PlayViewerInteractiveRepositoryImpl(
            getCurrentInteractiveUseCase = mockCurrentInteractiveUseCase,
            answerQuizUseCase = mockAnswerQUizUseCase,
            getInteractiveViewerLeaderboardUseCase = mockk(relaxed = true),
            mapper = mockMapper,
            dispatchers = testDispatcher,
            interactiveStorage = mockInteractiveStorage,
            postInteractiveTapUseCase = mockk(relaxed = true)
        )

    private val mockRepo = PlayViewerMockRepository.get(interactiveRepo = interactiveRepo)

    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    @Before
    fun setUp(){
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
    }

    @Test
    fun `given quiz is active and answer quiz is success, when click option, then user should have joined the game`() {
    }
}