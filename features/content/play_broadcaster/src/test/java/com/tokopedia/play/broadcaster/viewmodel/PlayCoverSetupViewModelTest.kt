package com.tokopedia.play.broadcaster.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.domain.usecase.GetOriginalProductImageUseCase
import com.tokopedia.play.broadcaster.domain.usecase.UploadImageToRemoteV2UseCase
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockImageTransformer
import com.tokopedia.play.broadcaster.testdouble.MockPlayCoverImageUtil
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.page.PlayBroPageSource
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverSetupViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 29/09/20
 */
class PlayCoverSetupViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineScopeRule = CoroutineTestRule()

    private val dispatchers = coroutineScopeRule.dispatchers

    private lateinit var coverDataStore: MockCoverDataStore
    private lateinit var broadcastScheduleDataStore: BroadcastScheduleDataStore
    private lateinit var titleDataStore: TitleDataStore
    private lateinit var tagsDataStore: TagsDataStore
    private lateinit var interactiveDataStore: InteractiveDataStore
    private lateinit var productTagDataStore: ProductTagDataStore

    private lateinit var channelConfigStore: ChannelConfigStore
    private lateinit var titleConfigStore: TitleConfigStore

    private val mockImageTransformer = MockImageTransformer()

    private val uploadImageUseCase: UploadImageToRemoteV2UseCase = mockk(relaxed = true)
    private val getOriginalProductImageUseCase: GetOriginalProductImageUseCase = mockk(relaxed = true)

    private lateinit var mockSetupDataStore: MockSetupDataStore

    private lateinit var viewModel: PlayCoverSetupViewModel

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()
        titleConfigStore = TitleConfigStoreImpl()

        coverDataStore = MockCoverDataStore(dispatchers)
        broadcastScheduleDataStore = BroadcastScheduleDataStoreImpl(dispatchers, mockk())
        titleDataStore = TitleDataStoreImpl(dispatchers, mockk())
        tagsDataStore = TagsDataStoreImpl(dispatchers, mockk())
        interactiveDataStore = InteractiveDataStoreImpl()
        productTagDataStore = ProductTagDataStoreImpl()
        mockSetupDataStore = MockSetupDataStore(
            coverDataStore,
            broadcastScheduleDataStore,
            titleDataStore,
            tagsDataStore,
            interactiveDataStore,
            productTagDataStore,
        )

        viewModel = PlayCoverSetupViewModel(
            productList = emptyList(),
            channelId = "",
            hydraConfigStore = HydraConfigStoreImpl(
                BroadcastingConfigStoreImpl(),
                channelConfigStore,
                ProductConfigStoreImpl(),
                titleConfigStore,
                BroadcastScheduleConfigStoreImpl(),
                AccountConfigStoreImpl(),
            ),
            dispatcher = dispatchers,
            setupDataStore = mockSetupDataStore,
            uploadImageUseCase = uploadImageUseCase,
            getOriginalProductImageUseCase = getOriginalProductImageUseCase,
            coverImageUtil = MockPlayCoverImageUtil(),
            coverImageTransformer = mockImageTransformer,
            account = ContentAccountUiModel.Empty.copy(id = "123"),
            pageSource = PlayBroPageSource.Live
        )
    }

    @Test
    fun `when get crop state, if there is not yet cover cropped, then it should return blank`() {
        Assertions
                .assertThat(viewModel.cropState)
                .isEqualTo(CoverSetupState.Blank)
    }

    @Test
    fun `when get cover uri, if there is not yet cover cropped, then it should return null`() {
        Assertions
                .assertThat(viewModel.coverUri)
                .isEqualTo(null)
    }

    @Test
    fun `when get cover uri, if there is cover, then it should return the uri of the cover`() {
        val uri: Uri = mockk(relaxed = true)
        val croppedState = CoverSetupState.Cropped.Draft(uri, CoverSource.Gallery)

        coverDataStore.setFullCover(
                PlayCoverUiModel(
                        croppedCover = croppedState,
                        state = SetupDataState.Draft
                )
        )

        Assertions
                .assertThat(croppedState.coverImage)
                .isEqualTo(uri)
    }

    @Test
    fun `when get cover source, if there is not yet cover cropped, then it should return none`() {
        Assertions
                .assertThat(viewModel.source)
                .isEqualTo(CoverSource.None)
    }

    @Test
    fun `when get cover source, if there is cover, then it should return the cover source`() {
        val source = CoverSource.Camera
        val croppedState = CoverSetupState.Cropped.Draft(mockk(relaxed = true), source)

        coverDataStore.setFullCover(
                PlayCoverUiModel(
                        croppedCover = croppedState,
                        state = SetupDataState.Draft
                )
        )

        Assertions
                .assertThat(croppedState.coverSource)
                .isEqualTo(source)
    }

    @Test
    fun `when get max title character allowed for cover, it should return the correct length`() {
        val maxChar = 5
        titleConfigStore.setMaxTitleChars(maxChar)

        Assertions
                .assertThat(viewModel.maxTitleChars)
                .isEqualTo(maxChar)
    }

    @Test
    fun `when check the validity of blank cover title, it should return false`() {
        val coverTitle = ""

        Assertions
                .assertThat(viewModel.isValidCoverTitle(coverTitle))
                .isEqualTo(false)
    }

    @Test
    fun `when check the validity of cover title that exceeds max chars, it should return false`() {
        val coverTitle = "abcde"
        val maxChar = 3
        titleConfigStore.setMaxTitleChars(maxChar)

        Assertions
                .assertThat(viewModel.isValidCoverTitle(coverTitle))
                .isEqualTo(false)
    }

    @Test
    fun `when check the validity of cover title that doesn't exceed max chars and not blank, it should return true`() {
        val coverTitle = "abcde"
        val maxChar = 6
        titleConfigStore.setMaxTitleChars(maxChar)

        Assertions
                .assertThat(viewModel.isValidCoverTitle(coverTitle))
                .isEqualTo(true)
    }

    @Test
    fun `given productId and resized image path, it should return the original image url when app calls getOriginalImageUrl`() {

        val mockOriginalImageUrlList = List(5) { "https://tokopedia.com/product/original_image/product_$it" }
        val mockResizedImageUrl = "https://tokopedia.com/product/resized_image/product_3"

        coEvery { getOriginalProductImageUseCase.executeOnBackground() } returns mockOriginalImageUrlList

        dispatchers.coroutineDispatcher.runBlockingTest {
            val originalImageUrl = viewModel.getOriginalImageUrl("", mockResizedImageUrl)

            Assertions
                .assertThat(originalImageUrl)
                .isEqualTo("https://tokopedia.com/product/original_image/product_3")
        }
    }

    @Test
    fun `uploadCover success`() {
        val coverSetupStateObserver = Observer<CoverSetupState> {}

        val mockUploadCoverSuccess = NetworkResult.Success(Unit)

        coEvery { uploadImageUseCase.executeOnBackground() } returns UploadResult.Success(uploadId = "123", fileUrl = "fileUrl", videoUrl = "videoUrl")
        coverDataStore.setUploadSelectedCoverResponse { mockUploadCoverSuccess }
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.observableCropState.observeForever(coverSetupStateObserver)
        viewModel.uploadCover()

        val result = viewModel.observableUploadCoverEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Success::class.java)

        viewModel.observableCropState.removeObserver(coverSetupStateObserver)
    }

    @Test
    fun `uploadCover fail`() {
        val coverSetupStateObserver = Observer<CoverSetupState> {}

        val mockException = Exception("network error")
        val mockUploadCoverFail = NetworkResult.Fail(mockException)

        coEvery { uploadImageUseCase.executeOnBackground() } returns UploadResult.Success(uploadId = "123", fileUrl = "fileUrl", videoUrl = "videoUrl")
        coverDataStore.setUploadSelectedCoverResponse { mockUploadCoverFail }
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.observableCropState.observeForever(coverSetupStateObserver)
        viewModel.uploadCover()

        val result = viewModel.observableUploadCoverEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(mockUploadCoverFail)

        viewModel.observableCropState.removeObserver(coverSetupStateObserver)
    }

    @Test
    fun `uploadCover error`() {
        val coverSetupStateObserver = Observer<CoverSetupState> {}

        val mockException = Exception("network error")
        val mockUploadCoverFail = NetworkResult.Fail(mockException)

        coEvery { uploadImageUseCase.executeOnBackground() } returns UploadResult.Success(uploadId = "123", fileUrl = "fileUrl", videoUrl = "videoUrl")
        coverDataStore.setUploadSelectedCoverResponse { throw mockException }
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.observableCropState.observeForever(coverSetupStateObserver)
        viewModel.uploadCover()

        val result = viewModel.observableUploadCoverEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isEqualTo(mockUploadCoverFail)

        viewModel.observableCropState.removeObserver(coverSetupStateObserver)
    }

    @Test
    fun `uploadCover error on uploadImageUseCase`() {
        val coverSetupStateObserver = Observer<CoverSetupState> {}

        val mockException = Exception("network error")

        coEvery { uploadImageUseCase.executeOnBackground() } returns UploadResult.Error("network error")
        coverDataStore.setUploadSelectedCoverResponse { throw mockException }
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.observableCropState.observeForever(coverSetupStateObserver)
        viewModel.uploadCover()

        val result = viewModel.observableUploadCoverEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Fail::class.java)

        viewModel.observableCropState.removeObserver(coverSetupStateObserver)
    }

    @Test
    fun `uploadCover error because of cover state is not draft`() {
        val coverSetupStateObserver = Observer<CoverSetupState> {}

        val mockException = Exception("network error")

        coEvery { uploadImageUseCase.executeOnBackground() } returns UploadResult.Error("network error")
        coverDataStore.setUploadSelectedCoverResponse { throw mockException }
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.GeneratedCover(
                    coverImage = "asdf"
                ),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.observableCropState.observeForever(coverSetupStateObserver)
        viewModel.uploadCover()

        val result = viewModel.observableUploadCoverEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Fail::class.java)

        viewModel.observableCropState.removeObserver(coverSetupStateObserver)
    }

    @Test
    fun `uploadCover error because validate image min size is failing`() {
        val coverSetupStateObserver = Observer<CoverSetupState> {}
        val mockUri = mockk<Uri>(relaxed = true)

        coEvery { mockUri.path } returns null
        mockImageTransformer.setTransformImageFromUriResponse { mockUri }

        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        viewModel.observableCropState.observeForever(coverSetupStateObserver)
        viewModel.uploadCover()

        val result = viewModel.observableUploadCoverEvent.getOrAwaitValue()

        Assertions
            .assertThat(result)
            .isInstanceOf(NetworkResult.Fail::class.java)

        viewModel.observableCropState.removeObserver(coverSetupStateObserver)
    }

    @Test
    fun `when app calls setCroppingCoverByUri, it should change the cover state to draft`() {
        /** To make sure that the state is changing from Uploaded to Draft */
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Uploaded)

        viewModel.setCroppingCoverByUri(
            coverUri = mockk(relaxed = true),
            source = CoverSource.Gallery
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Draft)
    }

    @Test
    fun `when app calls setCroppingCoverByBitmap, it should change the cover state to draft`() {
        /** To make sure that the state is changing from Uploaded to Draft */
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Uploaded)

        viewModel.setCroppingCoverByBitmap(
            bitmap = mockk(relaxed = true),
            source = CoverSource.Gallery
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Draft)
    }

    @Test
    fun `when app calls setCroppingProductCover, it should change the cover state to draft`() {
        /** To make sure that the state is changing from Uploaded to Draft */
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Uploaded)

        viewModel.setCroppingProductCover(
            productId = "123",
            imageUrl = "123",
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Draft)
    }

    @Test
    fun `when app calls setDraftCroppedCover, it should change the cover state to draft`() {
        /** To make sure that the state is changing from Uploaded to Draft */
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Uploaded)

        viewModel.setDraftCroppedCover(
            coverUri = mockk(relaxed = true)
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Draft)
    }

    @Test
    fun `when app calls removeCover, it should change the cover state to draft`() {
        /** To make sure that the state is changing from Uploaded to Draft */
        coverDataStore.setFullCover(
            PlayCoverUiModel(
                croppedCover = CoverSetupState.Cropped.Draft(
                    coverImage = mockk(relaxed = true),
                    coverSource = CoverSource.Gallery,
                ),
                state = SetupDataState.Uploaded
            )
        )

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Uploaded)

        viewModel.removeCover()

        Assertions.assertThat(viewModel.selectedCover?.state).isEqualTo(SetupDataState.Draft)
    }
}
