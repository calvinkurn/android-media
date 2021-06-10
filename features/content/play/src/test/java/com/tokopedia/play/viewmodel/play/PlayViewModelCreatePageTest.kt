package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.andThen
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.LikeSource
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
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val shareInfoBuilder = PlayShareInfoModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val totalViewBuilder = PlayTotalViewModelBuilder()
    private val likeBuilder = PlayLikeModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()
    private val statusInfoBuilder = PlayStatusInfoModelBuilder()

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
            videoMetaResult
                    .videoStream
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then like status value should be the same as in channel data`() {
        val totalLike = 5L
        val totalLikeFormatted = totalLike.toString()
        val isLiked = false

        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeBuilder.buildCompleteData(
                        status = likeBuilder.buildStatus(
                                totalLike = totalLike,
                                totalLikeFormatted = totalLikeFormatted,
                                isLiked = isLiked
                        )
                )
        )
        val expectedModel = likeBuilder.buildStatus(
                totalLike = totalLike,
                totalLikeFormatted = totalLikeFormatted,
                isLiked = isLiked,
                source = LikeSource.Storage
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            likeStatusResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then total view value should be the same as in channel data`() {
        val totalView = "5.8k"

        val channelData = channelDataBuilder.buildChannelData(
                totalViewInfo = totalViewBuilder.buildCompleteData(
                        totalView = totalView
                )
        )

        val expectedModel = totalViewBuilder.buildCompleteData(
                totalView = totalView
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            totalViewResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then partner info value should be the same as in channel data`() {
        val partnerName = "Penjual Sakti"
        val isFollowed = true

        val channelData = channelDataBuilder.buildChannelData(
                partnerInfo = partnerInfoBuilder.buildCompleteData(
                        basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(
                                name = partnerName
                        ),
                        followInfo = partnerInfoBuilder.buildPlayPartnerFollowInfo(
                                isFollowed = isFollowed
                        )
                )
        )

        val expectedModel = partnerInfoBuilder.buildCompleteData(
                basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(name = partnerName),
                followInfo = partnerInfoBuilder.buildPlayPartnerFollowInfo(isFollowed = isFollowed)
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            partnerInfoResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then cart info value should be the same as in channel data`() {
        val shouldShowCart = true
        val itemInCartCount = 95

        val channelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoBuilder.buildCompleteData(
                        shouldShow = shouldShowCart,
                        count = itemInCartCount
                )
        )

        val expectedModel = cartInfoBuilder.buildCompleteData(
                shouldShow = shouldShowCart,
                count = itemInCartCount
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            cartInfoResult
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
            quickReplyResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then share info value should be the same as in channel data`() {
        val shareContent = "Ayo buruan beli sekarang hanya di https://www.tokopedia.com"
        val shouldShowShare = shareContent.isNotEmpty()

        val channelData = channelDataBuilder.buildChannelData(
                shareInfo = shareInfoBuilder.build(
                        content = shareContent,
                        shouldShow = shouldShowShare
                )
        )

        val expectedModel = shareInfoBuilder.build(
                content = shareContent,
                shouldShow = shouldShowShare
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            shareInfoResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then channel info value should be the same as in channel data`() {
        val channelType = PlayChannelType.VOD
        val backgroundUrl = "https://tokopedia.com/play/channels"

        val channelData = channelDataBuilder.buildChannelData(
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = channelType,
                        backgroundUrl = backgroundUrl
                )
        )

        val expectedModel = channelInfoBuilder.buildChannelInfo(
                channelType = channelType,
                backgroundUrl = backgroundUrl
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            channelInfoResult
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
            pinnedResult
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `given channel data is set, when page is created, then status info value should be the same as in channel data`() {
        val statusType = PlayStatusType.Active
        val bannedTitle = "Anda di banned"
        val freezeTitle = "Channel ini dibekukan"

        val channelData = channelDataBuilder.buildChannelData(
                statusInfo = statusInfoBuilder.build(
                        statusType = statusType,
                        bannedModel = statusInfoBuilder.buildBannedModel(
                                title = bannedTitle,
                        ),
                        freezeModel = statusInfoBuilder.buildFreezeModel(
                                title = freezeTitle
                        )
                )
        )

        val expectedModel = statusInfoBuilder.build(
                statusType = statusType,
                bannedModel = statusInfoBuilder.buildBannedModel(
                        title = bannedTitle,
                ),
                freezeModel = statusInfoBuilder.buildFreezeModel(
                        title = freezeTitle
                )
        )

        givenPlayViewModelRobot(
        ) andWhen {
            createPage(channelData)
        } thenVerify {
            statusInfoResult
                    .isEqualTo(expectedModel)
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

            val expectedResult = Event(Unit)

            givenPlayViewModelRobot(
                playPreference = playPreference
            ) andWhen {
                createPage(channelData = channelData)
            } andThen {
                advanceTimeBy(5000)
            } thenVerify {
                onboardingResult
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
                onboardingResult
                    .hasNoValue()
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
                onboardingResult
                    .hasNoValue()
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
                onboardingResult
                    .hasNoValue()
            }
        }
    }
}