package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.isEqualToIgnoringFields
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.view.uimodel.action.ClickShareAction
import com.tokopedia.play.view.uimodel.event.CopyToClipboardEvent
import com.tokopedia.play.view.uimodel.event.OpenSharingOptionEvent
import com.tokopedia.play.view.uimodel.event.ShowInfoEvent
import com.tokopedia.play.view.uimodel.event.UiString
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 16, 2021
 */
class PlayViewModelShareExperienceTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataModelBuilder = PlayChannelDataModelBuilder()
    private val channelData = channelDataModelBuilder.buildChannelData()

    private val channelId = channelData.id
    private val channelType = channelData.channelDetail.channelInfo.channelType.value
    private val channelInfo = channelData.channelDetail.channelInfo
    private val shareInfo = channelData.channelDetail.shareInfo

    private val mockRemoteConfig: RemoteConfig = mockk(relaxed = true)
    private val mockPlayNewAnalytic: PlayNewAnalytic = mockk(relaxed = true)
    private val mockPlayShareExperience: PlayShareExperience = mockk(relaxed = true)

    private val testDispatcher = CoroutineTestDispatchers

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)

        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when user click share action & custom sharing is allowed, it should emit event to open universal bottom sheet`() {
        /** Prepare */
        every { mockPlayNewAnalytic.clickShareButton(any(), any()) } returns Unit
        coEvery { mockPlayNewAnalytic.impressShareBottomSheet(any(), any()) } returns Unit
        coEvery { mockPlayShareExperience.isCustomSharingAllow() } returns true

        val mockEvent = OpenSharingOptionEvent(
            title = channelInfo.title,
            coverUrl = channelInfo.coverUrl,
            userId = "",
            channelId = channelId
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            playAnalytic = mockPlayNewAnalytic,
            playShareExperience = mockPlayShareExperience,
        ) {
            createPage(channelData)
            focusPage(channelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickShareAction)
            }

            /** Verify */
            verify { mockPlayNewAnalytic.clickShareButton(channelId, channelType) }
            verify { mockPlayNewAnalytic.impressShareBottomSheet(channelId, channelType) }

            event.last().isEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user click share action & custom sharing is not allowed, it should emit event to open universal bottom sheet`() {
        /** Prepare */
        every { mockPlayNewAnalytic.clickShareButton(any(), any()) } returns Unit
        coEvery { mockPlayShareExperience.isCustomSharingAllow() } returns false

        val mockCopyEvent = CopyToClipboardEvent(
            shareInfo.content
        )
        val mockShowInfoEvent = ShowInfoEvent(
            UiString.Resource(123)
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            playAnalytic = mockPlayNewAnalytic,
            playShareExperience = mockPlayShareExperience,
        ) {
            createPage(channelData)
            focusPage(channelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickShareAction)
            }

            /** Verify */
            verify { mockPlayNewAnalytic.clickShareButton(channelId, channelType) }

            event[0].isEqualTo(mockCopyEvent)
            event[1].isEqualToIgnoringFields(mockShowInfoEvent, ShowInfoEvent::message)
        }
    }
}