package com.tokopedia.play.viewmodel.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.fake.FakePlayShareExperience
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.isEqualTo
import com.tokopedia.play.util.isEqualToIgnoringFields
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.universal_sharing.view.model.ShareModel
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

    private val fakePlayShareExperience = FakePlayShareExperience()

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
    fun `when user click share action & custom sharing is not allowed, it should emit event to copy the link`() {
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

    @Test
    fun `when user close sharing bottom sheet, it should send analytics close bottom sheet`() {
        /** Prepare */
        every { mockPlayNewAnalytic.closeShareBottomSheet(any(), any(), any()) } returns Unit
        every { mockPlayShareExperience.isScreenshotBottomSheet() } returns false

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
            it.recordEvent {
                submitAction(CloseSharingOptionAction)
            }

            /** Verify */
            verify { mockPlayNewAnalytic.closeShareBottomSheet(channelId, channelType, false) }
        }
    }

    @Test
    fun `when user take screenshot & custom share is allowed, it should emit event to open bottom sheet`() {
        /** Prepare */
        every { mockPlayNewAnalytic.takeScreenshotForSharing(any(), any()) } returns Unit
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
                submitAction(ScreenshotTakenAction)
            }

            /** Verify */
            verify { mockPlayNewAnalytic.takeScreenshotForSharing(channelId, channelType) }
            verify { mockPlayNewAnalytic.impressShareBottomSheet(channelId, channelType) }

            event.last().isEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user click share option, it should emit event to redirect to selected media`() {
        /** Prepare */
        every { mockPlayNewAnalytic.clickSharingOption(any(), any(), any(),any()) } returns Unit
        fakePlayShareExperience.setScreenshotBottomSheet(false)

        val shareModel = ShareModel.Whatsapp()

        val mockEvent = OpenSelectedSharingOptionEvent(
            linkerShareResult = null,
            shareModel = shareModel,
            ""
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            playAnalytic = mockPlayNewAnalytic,
            playShareExperience = fakePlayShareExperience,
        ) {
            createPage(channelData)
            focusPage(channelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickSharingOptionAction(shareModel))
            }

            /** Verify */
            verify { mockPlayNewAnalytic.clickSharingOption(channelId, channelType, shareModel.socialMediaName, false) }

            event.last().isEqualTo(mockEvent)
        }
    }

    @Test
    fun `when user click share option and error occur, it should emit event to copy link`() {
        /** Prepare */
        every { mockPlayNewAnalytic.clickSharingOption(any(), any(), any(),any()) } returns Unit
        fakePlayShareExperience.setScreenshotBottomSheet(false)
        fakePlayShareExperience.setThrowException(true)

        val shareModel = ShareModel.Whatsapp()

        val mockCopyEvent = CopyToClipboardEvent(
            shareInfo.content
        )

        val mockShowInfoEvent = ShowInfoEvent(
            UiString.Resource(123)
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            playAnalytic = mockPlayNewAnalytic,
            playShareExperience = fakePlayShareExperience,
        ) {
            createPage(channelData)
            focusPage(channelData)
        }

        robot.use {
            /** Test */
            val event = it.recordEvent {
                submitAction(ClickSharingOptionAction(shareModel))
            }

            /** Verify */
            verify { mockPlayNewAnalytic.clickSharingOption(channelId, channelType, shareModel.socialMediaName, false) }

            event[0].isEqualTo(mockCopyEvent)
            event[1].isEqualToIgnoringFields(mockShowInfoEvent, ShowInfoEvent::message)
        }
    }

    @Test
    fun `when user choose permission regarding universal bottom sheet, it should send analytics choose permission`() {
        /** Prepare */
        every { mockPlayNewAnalytic.clickSharePermission(any(), any(), any()) } returns Unit

        val label = "allow"

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            playAnalytic = mockPlayNewAnalytic,
        ) {
            createPage(channelData)
            focusPage(channelData)
        }

        robot.use {
            /** Test */
            it.recordEvent {
                submitAction(SharePermissionAction(label))
            }

            /** Verify */
            verify { mockPlayNewAnalytic.clickSharePermission(channelId, channelType, label) }
        }
    }
}