package com.tokopedia.play.broadcaster.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.domain.usecase.GetOriginalProductImageUseCase
import com.tokopedia.play.broadcaster.domain.usecase.UploadImageToRemoteV2UseCase
import com.tokopedia.play.broadcaster.model.ModelBuilder
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockImageTransformer
import com.tokopedia.play.broadcaster.testdouble.MockPlayCoverImageUtil
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.CarouselCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.CoverSource
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.state.SetupDataState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayCoverSetupViewModel
import io.mockk.mockk
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

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var productDataStore: ProductDataStore
    private lateinit var coverDataStore: MockCoverDataStore
    private lateinit var broadcastScheduleDataStore: BroadcastScheduleDataStore
    private lateinit var titleDataStore: TitleDataStore
    private lateinit var tagsDataStore: TagsDataStore

    private lateinit var channelConfigStore: ChannelConfigStore
    private lateinit var titleConfigStore: TitleConfigStore

    private val uploadImageUseCase: UploadImageToRemoteV2UseCase = mockk(relaxed = true)
    private val getOriginalProductImageUseCase: GetOriginalProductImageUseCase = mockk(relaxed = true)

    private lateinit var mockSetupDataStore: MockSetupDataStore

    private lateinit var viewModel: PlayCoverSetupViewModel

    private val modelBuilder = ModelBuilder()

    private val uploadCoverTitleException = IllegalStateException("error upload cover title")

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()
        titleConfigStore = TitleConfigStoreImpl()

        productDataStore = ProductDataStoreImpl(dispatcherProvider, mockk())
        coverDataStore = MockCoverDataStore(dispatcherProvider, uploadCoverTitleException)
        broadcastScheduleDataStore = BroadcastScheduleDataStoreImpl(dispatcherProvider, mockk())
        titleDataStore = TitleDataStoreImpl(dispatcherProvider, mockk(), mockk())
        tagsDataStore = TagsDataStoreImpl(dispatcherProvider, mockk())
        mockSetupDataStore = MockSetupDataStore(productDataStore, coverDataStore, broadcastScheduleDataStore, titleDataStore, tagsDataStore)

        viewModel = PlayCoverSetupViewModel(
                hydraConfigStore = HydraConfigStoreImpl(
                        channelConfigStore,
                        ProductConfigStoreImpl(),
                        titleConfigStore,
                        BroadcastScheduleConfigStoreImpl()
                ),
                dispatcher = dispatcherProvider,
                setupDataStore = mockSetupDataStore,
                uploadImageUseCase = uploadImageUseCase,
                getOriginalProductImageUseCase = getOriginalProductImageUseCase,
                coverImageUtil = MockPlayCoverImageUtil(),
                coverImageTransformer = MockImageTransformer()
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
    fun `when observe selected products, it should return the correct products`() {
        val productList = listOf(modelBuilder.buildProductData())
        productDataStore.setSelectedProducts(productList)

        val actual = viewModel.observableSelectedProducts.getOrAwaitValue()

        Assertions
                .assertThat(actual)
                .isEqualTo(productList.map { CarouselCoverUiModel.Product(ProductContentUiModel.createFromData(it)) })
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
}