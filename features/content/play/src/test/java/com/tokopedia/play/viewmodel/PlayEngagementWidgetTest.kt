package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEmpty
import com.tokopedia.play.util.assertFalse
import com.tokopedia.play.util.assertInstanceOf
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 12/09/22
 */
@ExperimentalCoroutinesApi
class PlayEngagementWidgetTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

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
    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val testDispatcher = coroutineTestRule.dispatchers


    //if only game exist
    //if only voucher exist
    //add each test for game
    //check if voucher list for coupon not empty
    //click voucher open coupon sheet
    //close voucher
    //visibility check

    /**
     * Voucher
     */
    @Test
    fun `voucher - when empty hide`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.tagItems.voucher.voucherList.assertEmpty()
            state.engagement.data.assertEmpty()
            state.engagement.shouldShow.assertFalse()
        }
    }

    /**
     * Game
     */
    @Test
    fun `game - when empty hide`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.interactive.interactive.assertInstanceOf<InteractiveUiModel.Unknown>()
            state.engagement.data.assertEmpty()
            state.engagement.shouldShow.assertFalse()
        }
    }

    /**
     * Both
     */

}