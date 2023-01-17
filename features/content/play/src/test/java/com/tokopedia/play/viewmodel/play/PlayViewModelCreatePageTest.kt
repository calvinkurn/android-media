package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andThen
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 20/02/20
 */
@ExperimentalCoroutinesApi
class PlayViewModelCreatePageTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

    private val uiModelBuilder = UiModelBuilder.get()

    @Test
    fun `given channel data is set, when page is created, then video stream value should be the same as in channel data`() {
        val id = "1"
        val videoOrientation = VideoOrientation.Horizontal(1, 2)

        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoStream = videoModelBuilder.buildVideoStream(
                                id = id,
                                orientation = videoOrientation
                        )
                )
        )
        val expectedModel = videoModelBuilder.buildVideoStream(
                id = id,
                orientation = videoOrientation
        )

        val robot = createPlayViewModelRobot()

        robot.use {
            val state = it.recordState {
                createPage(channelData)
            }

            it.viewModel.observableVideoMeta.getOrAwaitValue()
                .videoStream
                .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then quick replies value should be the same as in channel data`() {
        val quickReplyList = listOf("Wah keren", "Bagus Sekali", "<3")

        val channelData = channelDataBuilder.buildChannelData(
                quickReplyInfo = uiModelBuilder.buildQuickReply(quickReplyList)
        )

        val expectedModel = uiModelBuilder.buildQuickReply(quickReplyList)

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData

        val robot = createPlayViewModelRobot(
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                createPage(channelData)
            }

            state.quickReply.isEqualTo(expectedModel)
        }
    }

    @Test
    fun `default - given channel data is set, when page is created first channel show coachmark`() {
        coroutineTestRule.runBlockingTest {
            val playPreference: PlayPreference = mockk(relaxed = true)
            every { playPreference.isCoachMark(any()) } returns true

            val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                    videoPlayer = videoModelBuilder.buildCompleteGeneralVideoPlayer()
                )
            )

            givenPlayViewModelRobot(
                playPreference = playPreference
            ) andWhen {
                createPage(channelData = channelData)
            } andThen {
                advanceTimeBy(5000)
            } thenVerify {
                viewModel.observableOnboarding.getOrAwaitValue().peekContent()
                    .isEqualTo(Unit)
            }
        }
    }
}
