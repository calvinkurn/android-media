package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.robot.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.thenVerify
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PiPState
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelPiPTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

    @Test
    fun `when watch in pip mode, the mode field should also be watch in pip mode`() {
        givenPlayViewModelRobot(
        ) andWhen {
            setPiPState(PiPState.InPiP(PiPMode.WatchInPiP))
        } thenVerify {
            viewModel.pipState.mode
                    .isEqualTo(PiPMode.WatchInPiP)
        }
    }

    @Test
    fun `when open browsing other page in pip mode, the mode field should also be open browsing other page in pip mode`() {
        val mode = PiPMode.BrowsingOtherPage(OpenApplinkUiModel(""))
        givenPlayViewModelRobot(
        ) andWhen {
            setPiPState(PiPState.InPiP(mode))
        } thenVerify {
            viewModel.pipState.mode
                    .isEqualTo(mode)
        }
    }

    @Test
    fun `when stop pip mode, the mode field should also be stop pip mode`() {
        givenPlayViewModelRobot(
        ) andWhen {
            setPiPState(PiPState.Stop)
        } thenVerify {
            viewModel.pipState
                    .isEqualTo(PiPState.Stop)

            viewModel.pipState.mode
                    .isEqualTo(null)
        }
    }

    @Test
    fun `given remote config is enabled, when channel is youtube, then pip is not allowed`() {
        val remoteConfig: RemoteConfig = mockk(relaxed = true)
        every { remoteConfig.getBoolean(any(), any()) } returns true

        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoPlayer = videoModelBuilder.buildYouTubeVideoPlayer("123")
                )
        )

        givenPlayViewModelRobot(
                remoteConfig = remoteConfig
        ) {
            createPage(channelData)
        } andWhen {
            isPiPAllowed()
        } thenVerify { result ->
            result.assertFalse()
        }
    }

    @Test
    fun `given remote config is enabled, when channel is not youtube, then pip is allowed`() {
        val remoteConfig: RemoteConfig = mockk(relaxed = true)
        every { remoteConfig.getBoolean(any(), any()) } returns true

        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoPlayer = videoModelBuilder.buildIncompleteGeneralVideoPlayer()
                )
        )

        givenPlayViewModelRobot(
                remoteConfig = remoteConfig
        ) {
            createPage(channelData)
        } andWhen {
            isPiPAllowed()
        } thenVerify { result ->
            result.assertTrue()
        }
    }

    @Test
    fun `given remote config is disabled, when channel is youtube, then pip is not allowed`() {
        val remoteConfig: RemoteConfig = mockk(relaxed = true)
        every { remoteConfig.getBoolean(any(), any()) } returns false

        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoPlayer = videoModelBuilder.buildYouTubeVideoPlayer("123")
                )
        )

        givenPlayViewModelRobot(
                remoteConfig = remoteConfig
        ) {
            createPage(channelData)
        } andWhen {
            isPiPAllowed()
        } thenVerify { result ->
            result.assertFalse()
        }
    }

    @Test
    fun `given remote config is disabled, when channel is not youtube, then pip is not allowed`() {
        val remoteConfig: RemoteConfig = mockk(relaxed = true)
        every { remoteConfig.getBoolean(any(), any()) } returns false

        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoPlayer = videoModelBuilder.buildIncompleteGeneralVideoPlayer()
                )
        )

        givenPlayViewModelRobot(
                remoteConfig = remoteConfig
        ) {
            createPage(channelData)
        } andWhen {
            isPiPAllowed()
        } thenVerify { result ->
            result.assertFalse()
        }
    }
}
