package com.tokopedia.play.broadcaster.viewmodel.setup.cover

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.model.setup.product.ProductSetupUiModelBuilder
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.autogeneratedcover.GeneratedCoverUiModel
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.play.broadcaster.util.assertNotEmpty
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class PlayBroSetupCoverViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers

    private val mockRepo: PlayBroadcastRepository = mockk(relaxed = true)
    private val mockUserSessionInterface: UserSessionInterface = mockk(relaxed = true)
    private val mockHydraSharedPreferences: HydraSharedPreferences = mockk(relaxed = true)
    private val mockDataStore: PlayBroadcastDataStore = mockk(relaxed = true)

    private val mockEntryPoint = "prep page"
    private val mockChannelTitle = "Title"
    private val mockChannelAuthor = "Shop"
    private val mockPosition = 1
    private val mockCoverImage: Uri = mockk(relaxed = true)
    private val mockUiBuilder = UiModelBuilder()
    private val mockUiProductBuilder = ProductSetupUiModelBuilder()
    private val mockContentAccount = mockUiBuilder.buildAccountListModel()
    private val mockProductTag = mockUiProductBuilder.buildProductTagSectionList()
    private val mockImageGeneratorMapper =
        ImageGeneratorMapperMock(mockProductTag, mockContentAccount.first().iconUrl)
    private val mockImageGeneratorPreviewSelected = mockImageGeneratorMapper.getImagePreview(
        channelTitle = mockChannelTitle,
        channelAuthor = mockChannelAuthor,
        selectedPosition = mockPosition
    )
    private val mockImageGeneratorPreviewUnselected = mockImageGeneratorMapper.getImagePreview(
        isUnselected = true,
        channelTitle = mockChannelTitle,
        channelAuthor = mockChannelAuthor,
        selectedPosition = mockPosition
    )
    private val mockImageGeneratorArgs =
        ImageGeneratorArgsMock(mockImageGeneratorPreviewSelected[mockPosition].imageUrl)
    private val mockPlayCoverUi = mockUiBuilder.buildPlayCoverUiModel(
        croppedCover = CoverSetupState.Cropped.Uploaded(
            localImage = null,
            coverImage = mockCoverImage,
            coverSource = CoverSource.Product(id = "123"),
        ), state = SetupDataState.Draft
    )
    private val mockGeneratedUiModel =
        GeneratedCoverUiModel(imageUrl = "url.image", sourceId = "23j38")

    @Test
    fun `when user SetUploadImageCover`() {
        coEvery {
            mockHydraSharedPreferences.getSavedSelectedAutoGeneratedCover(
                mockContentAccount.first().id,
                mockEntryPoint
            )
        } returns -1
        coEvery {
            mockDataStore.getSetupDataStore().getObservableSelectedCover()
        } returns MutableLiveData(mockPlayCoverUi)

        val robot = PlayBroSetupCoverViewModelRobot(
            dispatchers = testDispatcher,
            channelTitle = "Title",
            channelId = "123",
            contentAccount = mockContentAccount.first(),
            dataStore = mockDataStore,
            entryPoint = mockEntryPoint,
            sharedPref = mockHydraSharedPreferences,
            repo = mockRepo,
            userSession = mockUserSessionInterface,
        )

        robot.recordEvent {
            handleSetUploadImageCoverTest(mockPlayCoverUi)
            mockHydraSharedPreferences.getSavedSelectedAutoGeneratedCover(
                mockContentAccount.first().id,
                mockEntryPoint
            ).assertEqualTo(-1)
            observableCoverTest.getOrAwaitValue().assertEqualTo(mockPlayCoverUi)
        }
    }

    @Test
    fun `when user CheckButtonState and RemoveStateAutoGeneratedCover`() {
        val robot = PlayBroSetupCoverViewModelRobot(
            dispatchers = testDispatcher,
            channelTitle = mockChannelTitle,
            channelId = "123",
            contentAccount = mockContentAccount.first(),
            dataStore = mockDataStore,
            entryPoint = mockEntryPoint,
            sharedPref = mockHydraSharedPreferences,
            repo = mockRepo,
            userSession = mockUserSessionInterface,
        )

        val event = robot.recordEvent {
            handleCheckButtonStateTest()
        }
        event.assertNotEmpty()
    }

    @Test
    fun `when user SetUploadAutoGeneratedCover`() {
        coEvery { mockDataStore.getSetupDataStore().getProductTag() } returns mockProductTag

        val robot = PlayBroSetupCoverViewModelRobot(
            dispatchers = testDispatcher,
            channelTitle = mockChannelTitle,
            channelId = "123",
            contentAccount = mockContentAccount.first(),
            dataStore = mockDataStore,
            entryPoint = mockEntryPoint,
            sharedPref = mockHydraSharedPreferences,
            repo = mockRepo,
            userSession = mockUserSessionInterface,
        )

        val state = robot.getAutoGeneratedCover {
            handleGetAutoGeneratedImageCoverPreviewTest()
            handleSelectAutoGeneratedCoverTest(mockPosition)
        }
        state.assertEqualTo(mockImageGeneratorPreviewSelected)
    }

    @Test
    fun `when user SaveSelectedImageCover and success`() {
        coEvery { mockDataStore.getSetupDataStore().getProductTag() } returns mockProductTag
        coEvery { mockRepo.getGeneratedImageCover(mockImageGeneratorArgs.getArgs()) } returns mockGeneratedUiModel

        val robot = PlayBroSetupCoverViewModelRobot(
            dispatchers = testDispatcher,
            channelTitle = mockChannelTitle,
            channelId = "123",
            contentAccount = mockContentAccount.first(),
            dataStore = mockDataStore,
            entryPoint = mockEntryPoint,
            sharedPref = mockHydraSharedPreferences,
            repo = mockRepo,
            userSession = mockUserSessionInterface,
        )

        val state = robot.getAutoGeneratedCover {
            handleGetAutoGeneratedImageCoverPreviewTest()
            handleSelectAutoGeneratedCoverTest(mockPosition)
            handleSetUploadAutoGeneratedCoverTest()
        }
        state.assertEqualTo(mockImageGeneratorPreviewSelected)
    }

    @Test
    fun `when user SaveSelectedImageCover and fails`() {
        coEvery { mockDataStore.getSetupDataStore().getProductTag() } returns mockProductTag
        coEvery { mockRepo.getGeneratedImageCover(mockImageGeneratorArgs.getArgs()) } throws mockUiBuilder.buildException()

        val robot = PlayBroSetupCoverViewModelRobot(
            dispatchers = testDispatcher,
            channelTitle = mockChannelTitle,
            channelId = "123",
            contentAccount = mockContentAccount.first(),
            dataStore = mockDataStore,
            entryPoint = mockEntryPoint,
            sharedPref = mockHydraSharedPreferences,
            repo = mockRepo,
            userSession = mockUserSessionInterface,
        )

        val event = robot.recordEvent {
            handleGetAutoGeneratedImageCoverPreviewTest()
            handleSelectAutoGeneratedCoverTest(mockPosition)
            handleSetUploadAutoGeneratedCoverTest()
        }
        event.assertNotEmpty()
    }

    @Test
    fun `when user UnSelectAutoGeneratedCover`() {
        coEvery { mockDataStore.getSetupDataStore().getProductTag() } returns mockProductTag
        coEvery { mockRepo.getGeneratedImageCover(mockImageGeneratorArgs.getArgs()) } returns mockGeneratedUiModel

        val robot = PlayBroSetupCoverViewModelRobot(
            dispatchers = testDispatcher,
            channelTitle = mockChannelTitle,
            channelId = "123",
            contentAccount = mockContentAccount.first(),
            dataStore = mockDataStore,
            entryPoint = mockEntryPoint,
            sharedPref = mockHydraSharedPreferences,
            repo = mockRepo,
            userSession = mockUserSessionInterface,
        )

        val state = robot.getAutoGeneratedCover {
            handleGetAutoGeneratedImageCoverPreviewTest()
            handleSelectAutoGeneratedCoverTest(mockPosition)
            handleSetUploadAutoGeneratedCoverTest()
            handleUnSelectAutoGeneratedCoverTest()
            handleRemoveStateAutoGeneratedCoverTest()
        }
        state.assertEqualTo(mockImageGeneratorPreviewUnselected)
    }

}
