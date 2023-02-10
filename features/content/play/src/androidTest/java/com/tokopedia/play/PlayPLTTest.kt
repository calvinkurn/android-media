package com.tokopedia.play

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.performance.PlayPerformanceDataFileUtils
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayGeneralVideoPlayerParams
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.TestRepeatRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by mzennis on 15/05/20.
 */
class PlayPLTTest {


    @get:Rule
    var testRepeatRule: TestRepeatRule = TestRepeatRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)
    private val socket: PlayWebSocket = mockk(relaxed = true)
    private val mockChannelStorage = mockk<PlayChannelStateStorage>(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelCount = 1

    private val channelIds = List(channelCount) { it.toString() }

    private val channelData = List(channelCount) {
        uiModelBuilder.buildChannelData(
            id = it.toString(),
            tagItems = uiModelBuilder.buildTagItem(
                product = uiModelBuilder.buildProductModel(
                    canShow = true,
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
                    "", VideoOrientation.Vertical, "Video Keren"
                ),
            ),
        )
    }

    init {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(channelData, "")
        every { mockChannelStorage.getChannelList() } returns channelIds
        every { mockChannelStorage.getData(any()) } answers {
            val channelId = (invocation.args[0] as String).toInt()
            channelData[channelId]
        }
    }

    @Before
    fun setup() {
        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )
    }

    @Test
    fun testPageLoadTimePerformance() {
        val robot = PlayActivityRobot(0.toString())

        robot.scenario.onActivity {
            val pageMonitoring = it.getPerformanceMonitoring()
            val videoLatency = it.activeFragment?.getVideoLatency().orZero()
            PlayPerformanceDataFileUtils(
                activity = it,
                testCaseName = TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE,
                performanceData = pageMonitoring.getPltPerformanceData(),
                videoLatencyDuration = videoLatency
            ).writeReportToFile()
        }
    }

    companion object {

        const val TEST_CASE_PAGE_LOAD_TIME_PERFORMANCE = "play_test_case_page_load_time"
    }
}
