package com.tokopedia.play.viewmodel.follow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertTrue
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.needFollow
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 30/11/22
 */
@ExperimentalCoroutinesApi
class PlayFollowPopUpTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val partnerInfoModelBuilder = PlayPartnerInfoModelBuilder()

    private val videoBuilder = PlayVideoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live),
        ),
        videoMetaInfo = videoBuilder.buildVideoMeta(
            videoPlayer = videoBuilder.buildCompleteGeneralVideoPlayer(),
            videoStream = videoBuilder.buildVideoStream(orientation = VideoOrientation.Vertical)
        ),
        partnerInfo = partnerInfoModelBuilder.buildPlayPartnerInfo(),
    )

    private val repo: PlayViewerRepository = mockk(relaxed = true)
    private val playPreference: PlayPreference = mockk(relaxed = true)

    private val popUpConfig = channelInfoBuilder.buildPopUpConfig()

    @Test
    fun `first fetch - no op, visited less than 24h` () {
        val isShown = true
        val isFollowed = false

        coEvery { repo.getIsFollowingPartner(any()) } returns isFollowed
        every { playPreference.isFollowPopup(any()) } returns isShown

        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            playPreference = playPreference
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.partner.needFollow.assertTrue()
            state.bottomInsets.isAnyShown.assertFalse()
            state.interactive.isPlaying.assertFalse()
            testDispatcher.coroutineDispatcher.advanceTimeBy(popUpConfig.duration)
            state.followPopUp.assertTrue()
        }
    }
}
