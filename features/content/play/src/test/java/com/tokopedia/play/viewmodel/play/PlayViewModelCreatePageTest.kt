package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
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

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()

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
    fun `get count comment in focus page - live means no call` (){
        val channelData = channelDataBuilder.buildChannelData(
            channelDetail = channelInfoBuilder.buildChannelDetail()
        )

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData

        val robot = createPlayViewModelRobot(
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                createPage(channelData)
                focusPage(channelData)
            }
            state.channel.commentConfig.shouldShow.assertFalse() //default
        }
        coVerify { repo.getCountComment(any()) wasNot called }
    }

    @Test
    fun `get count comment in focus page - VOD return to not show` (){
        val channelData = channelDataBuilder.buildChannelData(
            channelDetail = channelInfoBuilder.buildChannelDetail(
                channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.VOD),
                commentUiModel = channelInfoBuilder.buildCommentConfig()
            )
        )

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData
        coEvery { repo.getCountComment(any()) } returns channelData.channelDetail.commentConfig

        val robot = createPlayViewModelRobot(
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                createPage(channelData)
                focusPage(channelData)
            }
            state.channel.commentConfig.shouldShow.assertEqualTo(channelData.channelDetail.commentConfig.shouldShow) //default
            state.channel.commentConfig.total.assertEqualTo(channelData.channelDetail.commentConfig.total) //default
        }

        coVerify { repo.getCountComment(any()) }
    }

    @Test
    fun `get count comment in focus page - VOD return to show with number` (){
        val channelData = channelDataBuilder.buildChannelData(
            channelDetail = channelInfoBuilder.buildChannelDetail(
                channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.VOD),
                commentUiModel = channelInfoBuilder.buildCommentConfig(shouldShow = true, count = "125,5 rb")
            )
        )

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData
        coEvery { repo.getCountComment(any()) } returns channelData.channelDetail.commentConfig

        val robot = createPlayViewModelRobot(
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                createPage(channelData)
                focusPage(channelData)
            }
            state.channel.commentConfig.shouldShow.assertEqualTo(channelData.channelDetail.commentConfig.shouldShow)
            state.channel.commentConfig.total.assertEqualTo(channelData.channelDetail.commentConfig.total)
        }

        coVerify { repo.getCountComment(any()) }
    }

    @Test
    fun `get count comment in focus page - failed - vod - return default` () {
        val channelData = channelDataBuilder.buildChannelData(
            channelDetail = channelInfoBuilder.buildChannelDetail(
                channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.VOD),
                commentUiModel = channelInfoBuilder.buildCommentConfig()
            )
        )

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData
        coEvery { repo.getCountComment(any()) } throws MessageErrorException()

        val robot = createPlayViewModelRobot(
            repo = repo
        )

        robot.use {
            val state = it.recordState {
                createPage(channelData)
                focusPage(channelData)
            }
            state.channel.commentConfig.shouldShow.assertFalse() //default
            state.channel.commentConfig.total.assertEqualTo("") //default
        }

        coVerify { repo.getCountComment(any()) }
    }

    @Test
    fun `check is logged in vm is returning expected` () {
        val channelData = channelDataBuilder.buildChannelData(
            channelDetail = channelInfoBuilder.buildChannelDetail(
                channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.VOD),
                commentUiModel = channelInfoBuilder.buildCommentConfig()
            )
        )

        val repo: PlayViewerRepository = mockk(relaxed = true)
        every { repo.getChannelData(any()) } returns channelData

        val robot = createPlayViewModelRobot(
            repo = repo
        )

        val expected = false

        robot.use {
            it.createPage(channelData)
            it.focusPage(channelData)
            it.setLoggedIn(expected)
            it.viewModel.isLoggedIn.assertFalse()
        }
    }
}
