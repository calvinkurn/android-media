package com.tokopedia.play.uitest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.annotations.UiTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author by astidhiyaa on 25/11/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@UiTest
class PlayStatusUiTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12669"

    private val socket: PlayWebSocket = mockk(relaxed = true)

    private var status = PlayStatusType.Active
    private var source = PlayStatusSource.Network

    private val freezeStatus = """
        {
            "type":"FREEZE",
            "data":{
              "channel_id": "12269",
              "is_freeze":true,
              "timestamp":1592392273000
            }
        }
    """.trimIndent()

    private val channelDetail = PagingChannel(
        channelList = listOf(
            uiModelBuilder.buildChannelData(
                id = channelId,
                tagItems = uiModelBuilder.buildTagItem(
                    product = uiModelBuilder.buildProductModel(
                        canShow = true,
                    )
                ),
                status = uiModelBuilder.buildStatus(
                    config = uiModelBuilder.buildStatusConfig(),
                    channelStatus = PlayChannelStatus(
                        statusType = status,
                        statusSource = source,
                        waitingDuration = 100
                    )
                ),
                videoMetaInfo = PlayVideoMetaInfoUiModel(
                    videoPlayer = PlayVideoPlayerUiModel.General.Incomplete(
                        params = PlayGeneralVideoPlayerParams(
                            videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                            buffer = PlayBufferControl(),
                            lastMillis = null,
                        )
                    ),
                    videoStream = PlayVideoStreamUiModel(
                        "", VideoOrientation.Vertical, "Iklan Popmie"
                    ),
                ),
            )
        ),
        cursor = "",
    )

    private fun createRobot() = PlayActivityRobot(channelId, 3000, isYouTube = false)


    init {
        coEvery { repo.getChannels(any(), any()) } returns channelDetail
        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )
    }

    @Test
    fun firstChannel_error() {
        coEvery { repo.getChannels(any(), any()) } throws MessageErrorException("Not Found")

        val robot = createRobot()
        robot.isErrorViewAvailable()
    }

    @Test
    fun firstChannel_archived() {
        status = PlayStatusType.Archived
        coEvery { repo.getChannels(any(), any()) } returns channelDetail

        val robot = createRobot()
        robot.isErrorViewAvailable()
    }

    @Test
    fun firstChannel_freezeFromSocket() {
        every { socket.listenAsFlow() } returns MutableStateFlow<WebSocketAction>(
            WebSocketAction.NewMessage(
                Gson().fromJson(freezeStatus, WebSocketResponse::class.java)
            )
        )

        val robot = createRobot()
        robot.endViewIsAvailable(channelDetail.channelList.firstOrNull()?.status?.config?.freezeModel?.title.orEmpty())
    }

    @Test
    fun nextChannel_archived() {
        val nextChannel = uiModelBuilder.buildChannelData(
            id = channelId,
            tagItems = uiModelBuilder.buildTagItem(
                product = uiModelBuilder.buildProductModel(
                    canShow = true,
                )
            ),
            status = uiModelBuilder.buildStatus(
                config = uiModelBuilder.buildStatusConfig(),
                channelStatus = PlayChannelStatus(
                    statusType = PlayStatusType.Archived,
                    statusSource = PlayStatusSource.Network,
                    waitingDuration = 100
                )
            ),
            videoMetaInfo = PlayVideoMetaInfoUiModel(
                videoPlayer = PlayVideoPlayerUiModel.General.Incomplete(
                    params = PlayGeneralVideoPlayerParams(
                        videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                        buffer = PlayBufferControl(),
                        lastMillis = null,
                    )
                ),
                videoStream = PlayVideoStreamUiModel(
                    "", VideoOrientation.Vertical, "Iklan Popmie"
                ),
            ),
        )

        val newList = channelDetail.channelList.toMutableList().apply {
            add(1, nextChannel)
        }
        coEvery { repo.getChannels(any(), any()) } returns channelDetail.copy(channelList = newList)

        val robot = createRobot()
        robot.endViewIsAvailable(title = newList.lastOrNull()?.status?.config?.archivedModel?.title.orEmpty())
    }
}
