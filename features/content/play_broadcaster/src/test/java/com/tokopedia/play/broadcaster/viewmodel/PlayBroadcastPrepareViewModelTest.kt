package com.tokopedia.play.broadcaster.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.model.UiModelBuilder
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.util.*
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 28/09/20
 */
class PlayBroadcastPrepareViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var channelConfigStore: ChannelConfigStore

    private lateinit var coverDataStore: MockCoverDataStore
    private lateinit var broadcastScheduleDataStore: BroadcastScheduleDataStore
    private lateinit var titleDataStore: TitleDataStore
    private val mockTitleDataStore: TitleDataStore = mockk(relaxed = true)
    private lateinit var tagsDataStore: TagsDataStore
    private lateinit var interactiveDataStore: InteractiveDataStore
    private lateinit var productTagDataStoreImpl: ProductTagDataStoreImpl
    private lateinit var mockSetupDataStore: MockSetupDataStore
    private lateinit var dataStore: PlayBroadcastDataStore
    private lateinit var mockHydraDataStore: HydraConfigStore
    private lateinit var mockBroadcastSetupDataStore: PlayBroadcastSetupDataStore

    private val playBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer(), TestUriParser(), mockk(relaxed = true))

    private lateinit var createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase

    private lateinit var userSession: UserSessionInterface

    private lateinit var viewModel: PlayBroadcastPrepareViewModel

    private val modelBuilder = UiModelBuilder()

    private val mockException = modelBuilder.buildException()

    private val authorId = "123"

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()

        coverDataStore = MockCoverDataStore(dispatcherProvider)
        broadcastScheduleDataStore = BroadcastScheduleDataStoreImpl(dispatcherProvider, mockk())
        titleDataStore = TitleDataStoreImpl(dispatcherProvider, mockk())
        tagsDataStore = TagsDataStoreImpl(dispatcherProvider, mockk())
        interactiveDataStore = InteractiveDataStoreImpl()
        productTagDataStoreImpl = ProductTagDataStoreImpl()
        mockSetupDataStore = MockSetupDataStore(
            coverDataStore,
            broadcastScheduleDataStore,
            titleDataStore,
            tagsDataStore,
            interactiveDataStore,
            productTagDataStoreImpl,
        )
        mockHydraDataStore = TestDoubleModelBuilder().buildHydraConfigStore()
        mockBroadcastSetupDataStore = TestDoubleModelBuilder().buildSetupDataStore(
            titleDataStore = mockTitleDataStore,
        )

        dataStore = PlayBroadcastDataStoreImpl(mockSetupDataStore)

        userSession = mockk(relaxed = true)
        every { userSession.shopId } returns "12345"

        channelConfigStore.setChannelId("12345")

        createLiveStreamChannelUseCase = mockk(relaxed = true)

        viewModel = PlayBroadcastPrepareViewModel(
            dispatcher = dispatcherProvider,
            hydraConfigStore = mockHydraDataStore,
            setupDataStore = mockBroadcastSetupDataStore,
            channelConfigStore = channelConfigStore,
            createLiveStreamChannelUseCase = createLiveStreamChannelUseCase,
            mDataStore = dataStore,
            playBroadcastMapper = playBroadcastMapper,
            sharedPref = mockk(relaxed = true),
        )
    }

    @Test
    fun `when max duration description is set, then it should return the correct description`() {
        val desc = "30 minutes"
        channelConfigStore.setMaxDurationDesc(desc)

        Assertions
                .assertThat(viewModel.maxDurationDesc)
                .isEqualTo(desc)
    }

    @Test
    fun `when max duration description is not set, then it should return empty string`() {
        Assertions
                .assertThat(viewModel.maxDurationDesc)
                .isEmpty()
    }

    @Test
    fun `when max title chars is set, then it should return the correct number`() {
        val maxTitleChars = 25
        mockHydraDataStore.setMaxTitleChars(maxTitleChars)

        Assertions
            .assertThat(viewModel.maxTitleChars)
            .isEqualTo(maxTitleChars)
    }

    @Test
    fun `when max title chars is not set, then it should return 38`() {
        Assertions
            .assertThat(viewModel.maxTitleChars)
            .isEqualTo(38)
    }

    @Test
    fun `when isFromSwitchAccount is changed, it should emit the updated value`() {
        viewModel.setFromSwitchAccount(true)

        Assertions
            .assertThat(viewModel.isFromSwitchAccount)
            .isEqualTo(true)

        viewModel.setFromSwitchAccount(false)

        Assertions
            .assertThat(viewModel.isFromSwitchAccount)
            .isEqualTo(false)
    }

    @Test
    fun `when setup data store is overwritten and then fetched, it should return the new setup data store detail`() {
        val newDataStore = mockk<PlayBroadcastSetupDataStore>(relaxed = true)
        viewModel.setDataFromSetupDataStore(newDataStore)

        Assertions
                .assertThat(mockSetupDataStore.isOverwritten)
                .isEqualTo(true)
    }

    @Test
    fun `when create livestream with no title, it should return error`() {
        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

    @Test
    fun `given cover validation before livestream, when cover is uploaded, it should return true`() {
        val mockUri = mockk<Uri>(relaxed = true)

        every { mockUri.toString().isEmpty() } returns false

        coverDataStore.setFullCover(PlayCoverUiModel(
            croppedCover = CoverSetupState.Cropped.Uploaded(mockUri, mockUri, CoverSource.Camera),
            state = SetupDataState.Uploaded
        ))

        viewModel.isCoverAvailable().assertTrue()
    }

    @Test
    fun `given cover validation before livestream, when cover is empty, it should return false`() {
        viewModel.isCoverAvailable().assertFalse()
    }

    @Test
    fun `given cover validation before livestream, when cover setup state is GeneratedCover and its exists, it should return true`() {
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.GeneratedCover(coverImage = "coverImage"),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.isCoverAvailable().assertTrue()
    }

    @Test
    fun `given cover validation before livestream, when cover setup state is GeneratedCover and its exists, it should return false`() {
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.GeneratedCover(coverImage = ""),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.isCoverAvailable().assertFalse()
    }

    @Test
    fun `when create livestream with title and valid cover, it should return success`() {
        coverDataStore.setFullCover(PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Uploaded(null, mockk(relaxed = true), CoverSource.Camera),
                state = SetupDataState.Uploaded
        ))
        titleDataStore.setTitle("any title")

        coEvery { createLiveStreamChannelUseCase.executeOnBackground() } returns modelBuilder.buildCreateLiveStreamGetMedia()

        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Success::class.java)
    }

    @Test
    fun `when create livestream with title and generated cover, it should return success`() {
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.GeneratedCover(coverImage = "coverImage"),
                state = SetupDataState.Uploaded
            )
        )
        titleDataStore.setTitle("any title")

        coEvery { createLiveStreamChannelUseCase.executeOnBackground() } returns modelBuilder.buildCreateLiveStreamGetMedia()

        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Success::class.java)
    }

    @Test
    fun `when create livestream with title and no cover, it should return success`() {
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Blank,
                state = SetupDataState.Draft,
            )
        )
        titleDataStore.setTitle("any title")

        coEvery { createLiveStreamChannelUseCase.executeOnBackground() } returns modelBuilder.buildCreateLiveStreamGetMedia()

        viewModel.createLiveStream()

        val result = viewModel.observableCreateLiveStream.value

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Fail::class.java)
    }

    /** Setup Title */
    @Test
    fun `when user successfully upload title, it should emit network result success`() {
        coEvery { mockTitleDataStore.uploadTitle(any(), any(), any()) } returns NetworkResult.Success(Unit)

        viewModel.uploadTitle(authorId, "Test Title")

        val result = viewModel.observableUploadTitleEvent.getOrAwaitValue()

        result.getContentIfNotHandled()?.assertEqualTo(NetworkResult.Success(Unit))
    }

    @Test
    fun `when user failed upload title, it should emit network result fail`() {
        coEvery { mockTitleDataStore.uploadTitle(any(), any(), any()) } returns NetworkResult.Fail(mockException)

        viewModel.uploadTitle(authorId, "Test Title")

        val result = viewModel.observableUploadTitleEvent.getOrAwaitValue()

        result.getContentIfNotHandled()?.assertEqualTo(NetworkResult.Fail(mockException))
    }

    @Test
    fun `when user failed upload title because of exception, it should emit network result fail`() {
        coEvery { mockTitleDataStore.uploadTitle(any(), any(), any()) } throws mockException

        viewModel.uploadTitle(authorId, "Test Title")

        val result = viewModel.observableUploadTitleEvent.getOrAwaitValue()

        Assertions
            .assertThat(result.getContentIfNotHandled())
            .isInstanceOf(NetworkResult.Fail::class.java)
    }
}
