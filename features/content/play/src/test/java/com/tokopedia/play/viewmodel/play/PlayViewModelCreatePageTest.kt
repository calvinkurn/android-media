package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.NoValueException
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.andThen
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.isInstanceOf
import com.tokopedia.play.util.throwsException
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.types.PlayStatusType
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.Event
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

    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

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

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.observableVideoMeta.getOrAwaitValue()
                    .videoStream
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then quick replies value should be the same as in channel data`() {
        val quickReplyList = listOf("Wah keren", "Bagus Sekali", "<3")

        val channelData = channelDataBuilder.buildChannelData(
                quickReplyInfo = quickReplyBuilder.build(quickReplyList)
        )

        val expectedModel = quickReplyBuilder.build(quickReplyList)

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.observableQuickReply.getOrAwaitValue()
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then pinned value should be the same as in channel data`() {
        val pinnedMessage = "Saksikan keseruan BTS di sini"
        val shouldShowPinnedProduct = pinnedMessage.isEmpty()

        val channelData = channelDataBuilder.buildChannelData(
                pinnedInfo = pinnedBuilder.buildInfo(
                        pinnedMessage = pinnedBuilder.buildPinnedMessage(
                                title = pinnedMessage
                        ),
                        pinnedProduct = pinnedBuilder.buildPinnedProduct(
                                shouldShow = shouldShowPinnedProduct
                        )
                )
        )

        val expectedModel = pinnedBuilder.buildPinnedMessage(
                title = pinnedMessage
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            viewModel.observablePinnedMessage.getOrAwaitValue()
                    .isEqualTo(expectedModel)

            viewModel.observablePinnedProduct.getOrAwaitValue()
                    .isEqualTo(channelData.pinnedInfo.pinnedProduct)
        }
    }

    @Test
    fun `given channel data is set, when page is created and has not shown onboarding before, should show one tap onboarding after 5s`() {
        coroutineTestRule.runBlockingTest {
            val playPreference: PlayPreference = mockk(relaxed = true)
            every { playPreference.isOnboardingShown(any()) } returns false

            val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                    videoPlayer = videoModelBuilder.buildCompleteGeneralVideoPlayer()
                )
            )

            val expectedResult = Unit

            givenPlayViewModelRobot(
                playPreference = playPreference
            ) andWhen {
                createPage(channelData = channelData)
            } andThen {
                advanceTimeBy(5000)
            } thenVerify {
                viewModel.observableOnboarding.getOrAwaitValue().peekContent()
                    .isEqualTo(expectedResult)
            }
        }
    }

    @Test
    fun `given channel data is set, when page is created and has shown onboarding before, should not show one tap onboarding after 5s`() {
        coroutineTestRule.runBlockingTest {
            val playPreference: PlayPreference = mockk(relaxed = true)
            every { playPreference.isOnboardingShown(any()) } returns true

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
                throwsException<NoValueException> {
                    viewModel.observableOnboarding.getOrAwaitValue()
                }
            }
        }
    }

    @Test
    fun `given channel data is set, when page is created and has not shown onboarding but video player type is youtube, should not show one tap onboarding after 5s`() {
        coroutineTestRule.runBlockingTest {
            val playPreference: PlayPreference = mockk(relaxed = true)
            every { playPreference.isOnboardingShown(any()) } returns false

            val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                    videoPlayer = videoModelBuilder.buildYouTubeVideoPlayer()
                )
            )

            givenPlayViewModelRobot(
                playPreference = playPreference
            ) andWhen {
                createPage(channelData = channelData)
            } andThen {
                advanceTimeBy(5000)
            } thenVerify {
                throwsException<NoValueException> {
                    viewModel.observableOnboarding.getOrAwaitValue()
                }
            }
        }
    }

    @Test
    fun `given channel data is set, when page is created and has shown onboarding but video player type is youtube, should not show one tap onboarding after 5s`() {
        coroutineTestRule.runBlockingTest {
            val playPreference: PlayPreference = mockk(relaxed = true)
            every { playPreference.isOnboardingShown(any()) } returns true

            val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                    videoPlayer = videoModelBuilder.buildYouTubeVideoPlayer()
                )
            )

            givenPlayViewModelRobot(
                playPreference = playPreference
            ) andWhen {
                createPage(channelData = channelData)
            } andThen {
                advanceTimeBy(5000)
            } thenVerify {
                throwsException<NoValueException> {
                    viewModel.observableOnboarding.getOrAwaitValue()
                }
            }
        }
    }
}