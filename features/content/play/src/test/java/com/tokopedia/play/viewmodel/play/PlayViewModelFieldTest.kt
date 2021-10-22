package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.isFalse
import com.tokopedia.play.util.isTrue
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelFieldTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val likeBuilder = PlayLikeModelBuilder()
    private val statusInfoBuilder = PlayStatusInfoModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelReportBuilder = PlayChannelReportModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()
    private val responseBuilder = PlayResponseBuilder()

    @Test
    fun `given video player is set, when page is created and video player is retrieved, it should return the same one`() {
        val videoPlayer = videoModelBuilder.buildYouTubeVideoPlayer("abc")
        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(videoPlayer = videoPlayer)
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.videoPlayer
                    .isEqualTo(videoPlayer)
        }
    }

    @Test
    fun `given status info is set, when page is created and status type is retrieved, it should return the same one`() {
        val statusType = PlayStatusType.Banned
        val channelData = channelDataBuilder.buildChannelData(
                statusInfo = statusInfoBuilder.build(
                        statusType = statusType
                )
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.statusType
                    .isEqualTo(statusType)
        }
    }

    @Test
    fun `given video orientation is set, when page is created and video orientation is retrieved, it should return the same one`() {
        val videoOrientation = VideoOrientation.Horizontal(200, 37)
        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoStream = videoModelBuilder.buildVideoStream(
                                orientation = videoOrientation
                        )
                )
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.videoOrientation
                    .isEqualTo(videoOrientation)
        }
    }

    @Test
    fun `given channel type is set, when page is created and channel type is retrieved, it should return the same one`() {
        val channelType = PlayChannelType.Live
        val channelData = channelDataBuilder.buildChannelData(
                channelDetail = channelInfoBuilder.buildChannelDetail(
                        channelInfo = channelInfoBuilder.buildChannelInfo(
                                channelType = PlayChannelType.Live
                        )
                )
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.channelType
                    .isEqualTo(channelType)
        }
    }

    @Test
    fun `given status info is set, when page is created and isFreezeOrBanned is retrieved, it should return the correct value`() {
        val activeStatusInfo = statusInfoBuilder.build(
                statusType = PlayStatusType.Active
        )

        val freezeStatusInfo = statusInfoBuilder.build(
                statusType = PlayStatusType.Freeze
        )

        givenPlayViewModelRobot(
        ) andWhen {
            val channelData = channelDataBuilder.buildChannelData(
                    statusInfo = activeStatusInfo
            )
            createPage(channelData)
        } thenVerify {
            viewModel.isFreezeOrBanned.isFalse()
        } andWhen {
            val channelData = channelDataBuilder.buildChannelData(
                    statusInfo = freezeStatusInfo
            )
            createPage(channelData)
        } thenVerify {
            viewModel.isFreezeOrBanned.isTrue()
        }
    }

    @Test
    fun `given total view is set, when page is created and total view is retrieved, it should return correct value`() {
        val totalView = "5.91k"

        val channelData = channelDataBuilder.buildChannelData(
                channelReportInfo = channelReportBuilder.buildChannelReport(
                        totalViewFmt = totalView
                )
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.totalView.isEqualTo(totalView)
        }
    }

    @Test
    fun `given valid user id, when retrieved, then it should be correct`() {
        val validUserId = "123"

        givenPlayViewModelRobot{
            setMockUserId(validUserId)
        } thenVerify {
            viewModel.userId.isEqualTo(validUserId)
        }
    }
}