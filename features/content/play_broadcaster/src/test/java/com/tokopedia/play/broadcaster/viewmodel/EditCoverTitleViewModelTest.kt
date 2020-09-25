package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStore
import com.tokopedia.play.broadcaster.data.datastore.CoverDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStore
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStoreImpl
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.PlayCoverUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.TestCoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.state.CoverSetupState
import com.tokopedia.play.broadcaster.view.viewmodel.EditCoverTitleViewModel
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 25/09/20
 */
class EditCoverTitleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var productDataStore: ProductDataStore
    private lateinit var coverDataStore: MockCoverDataStore

    private lateinit var channelConfigStore: ChannelConfigStore
    private lateinit var coverConfigStore: CoverConfigStore

    private lateinit var mockSetupDataStore: MockSetupDataStore
    private lateinit var viewModel: EditCoverTitleViewModel

    private val uploadCoverTitleException = IllegalStateException("error upload cover title")

    class MockCoverDataStore(
            dispatcherProvider: CoroutineDispatcherProvider,
            private val uploadCoverTitleException: Throwable
    ) : CoverDataStore {

        private val realImpl = CoverDataStoreImpl(dispatcherProvider, mockk(), mockk())

        private var isSuccess: Boolean = false

        fun setIsSuccess(isSuccess: Boolean) {
            this.isSuccess = isSuccess
        }

        override fun getObservableSelectedCover(): LiveData<PlayCoverUiModel> {
            return realImpl.getObservableSelectedCover()
        }

        override fun getSelectedCover(): PlayCoverUiModel? {
            return realImpl.getSelectedCover()
        }

        override fun setFullCover(cover: PlayCoverUiModel) {
            realImpl.setFullCover(cover)
        }

        override fun updateCoverState(state: CoverSetupState) {
            realImpl.updateCoverState(state)
        }

        override fun updateCoverTitle(title: String) {
            realImpl.updateCoverTitle(title)
        }

        override suspend fun uploadSelectedCover(channelId: String): NetworkResult<Unit> {
            return realImpl.uploadSelectedCover(channelId)
        }

        override suspend fun uploadCoverTitle(channelId: String): NetworkResult<Unit> {
            return if (isSuccess) NetworkResult.Success(Unit)
            else NetworkResult.Fail(uploadCoverTitleException)
        }
    }

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()
        coverConfigStore = CoverConfigStoreImpl()

        productDataStore = ProductDataStoreImpl(dispatcherProvider, mockk())
        coverDataStore = MockCoverDataStore(dispatcherProvider, uploadCoverTitleException)
        mockSetupDataStore = MockSetupDataStore(productDataStore, coverDataStore)

        viewModel = EditCoverTitleViewModel(
                hydraConfigStore = HydraConfigStoreImpl(
                        channelConfigStore,
                        ProductConfigStoreImpl(),
                        coverConfigStore
                ),
                dispatcher = dispatcherProvider,
                setupDataStore = mockSetupDataStore
        )

        channelConfigStore.setChannelId("12345")
    }

    @Test
    fun `test edit title success`() {
        coverDataStore.setIsSuccess(true)
        viewModel.editTitle("abc")

        //Consume loading
        viewModel.observableUpdateTitle.getOrAwaitValue()

        val result = viewModel.observableUpdateTitle.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualTo(NetworkResult.Success(Unit))
    }

    @Test
    fun `test edit title failed`() {
        coverDataStore.setIsSuccess(false)
        viewModel.editTitle("abc")

        //Consume loading
        viewModel.observableUpdateTitle.getOrAwaitValue()

        val result = viewModel.observableUpdateTitle.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualTo(NetworkResult.Fail(uploadCoverTitleException))
    }

    @Test
    fun `test check not valid if cover title is blank`() {
        val isValid = viewModel.isValidCoverTitle("")

        Assertions
                .assertThat(isValid)
                .isEqualTo(false)
    }

    @Test
    fun `test check not valid if cover title exceeds maximum character`() {
        coverConfigStore.setMaxTitleChars(5)
        val isValid = viewModel.isValidCoverTitle("Barang Murah")

        Assertions
                .assertThat(isValid)
                .isEqualTo(false)
    }

    @Test
    fun `test check valid if cover title is not blank and does not exceed maximum character`() {
        coverConfigStore.setMaxTitleChars(20)
        val isValid = viewModel.isValidCoverTitle("Barang Murah")

        Assertions
                .assertThat(isValid)
                .isEqualTo(true)
    }

    @Test
    fun `test get current title`() {
        var currentTitle = ""
        val observer = Observer<String> {
            currentTitle = it
        }

        val newTitle = "abc"
        viewModel.observableCurrentTitle.observeForever(observer)
        viewModel.editTitle(newTitle)
        viewModel.observableCurrentTitle.removeObserver(observer)

        Assertions
                .assertThat(currentTitle)
                .isEqualTo(newTitle)

    }
}