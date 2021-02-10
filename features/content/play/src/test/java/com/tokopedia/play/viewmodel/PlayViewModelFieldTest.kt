package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.*
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.parent.givenParentViewModelRobot
import com.tokopedia.play.robot.parent.thenVerify
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelFieldTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val shareInfoBuilder = PlayShareInfoModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val totalViewBuilder = PlayTotalViewModelBuilder()
    private val likeBuilder = PlayLikeModelBuilder()
    private val statusInfoBuilder = PlayStatusInfoModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

    private val responseBuilder = PlayResponseBuilder()
    private val classBuilder = ClassBuilder()
    private val uiModelMapper = classBuilder.getPlayUiModelMapper()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given video player is set, when page is created and video player is retrieved, it should return the same one`() {
        val videoPlayer = videoModelBuilder.buildYouTubeVideoPlayer("abc")
        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(videoPlayer = videoPlayer)
        )

        val expectedModel = videoPlayer
        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            videoPlayerFieldResult
                    .isEqualTo(expectedModel)
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

        val expectedModel = statusType

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            statusTypeFieldResult
                    .isEqualTo(expectedModel)
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

        val expectedModel = videoOrientation

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            videoOrientationFieldResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel type is set, when page is created and channel type is retrieved, it should return the same one`() {
        val channelType = PlayChannelType.Live
        val channelData = channelDataBuilder.buildChannelData(
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = channelType
                )
        )

        val expectedModel = channelType

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            channelTypeFieldResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given like param is set, when page is created and like param is retrieved, it should return the same one`() {
        val likeParam = likeBuilder.buildParam(
                contentId = "123",
                contentType = 50,
                likeType = 7
        )
        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeBuilder.buildIncompleteData(
                        param = likeParam
                )
        )

        val expectedModel = likeParam

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            likeParamInfoFieldResult
                    .isEqualTo(likeParam)
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
    fun `given partner info is set, when page is created and partnerId is retrieved, it should return correct value`() {
        val partnerId = 123515L

        val channelData = channelDataBuilder.buildChannelData(
                partnerInfo = partnerInfoBuilder.buildIncompleteData(
                        basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(
                                id = partnerId
                        )
                )
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.partnerId.isEqualTo(partnerId)
        }
    }

    @Test
    fun `given total view is set, when page is created and total view is retrieved, it should return correct value`() {
        val totalView = "5.91k"

        val channelData = channelDataBuilder.buildChannelData(
                totalViewInfo = totalViewBuilder.buildCompleteData(
                        totalView = totalView
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