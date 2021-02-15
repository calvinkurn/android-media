package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayVideoModelBuilder
import com.tokopedia.play.robot.play.andWhen
import com.tokopedia.play.robot.play.givenPlayViewModelRobot
import com.tokopedia.play.robot.play.thenVerify
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.remoteconfig.RemoteConfig
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 11/02/21
 */
class PlayViewModelPiPTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when watch in pip mode, the mode field should also be watch in pip mode`() {
        givenPlayViewModelRobot(
        ) andWhen {
            setPiPMode(PiPMode.WatchInPip)
        } thenVerify {
            pipModeFieldResult
                    .isEqualTo(PiPMode.WatchInPip)
        }
    }

    @Test
    fun `when open browsing other page in pip mode, the mode field should also be open browsing other page in pip mode`() {
        givenPlayViewModelRobot(
        ) andWhen {
            setPiPMode(PiPMode.BrowsingOtherPage)
        } thenVerify {
            pipModeFieldResult
                    .isEqualTo(PiPMode.BrowsingOtherPage)
        }
    }

    @Test
    fun `when stop pip mode, the mode field should also be stop pip mode`() {
        givenPlayViewModelRobot(
        ) andWhen {
            setPiPMode(PiPMode.StopPip)
        } thenVerify {
            pipModeFieldResult
                    .isEqualTo(PiPMode.StopPip)
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
            result.isFalse()
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
            result.isTrue()
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
            result.isFalse()
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
            result.isFalse()
        }
    }
}