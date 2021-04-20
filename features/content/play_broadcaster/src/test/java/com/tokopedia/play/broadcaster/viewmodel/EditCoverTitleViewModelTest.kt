package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStore
import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStore
import com.tokopedia.play.broadcaster.data.datastore.ProductDataStoreImpl
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.EditCoverTitleViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.mockk
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

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var productDataStore: ProductDataStore
    private lateinit var coverDataStore: MockCoverDataStore

    private lateinit var channelConfigStore: ChannelConfigStore
    private lateinit var coverConfigStore: CoverConfigStore
    private lateinit var broadcastScheduleDataStore: BroadcastScheduleDataStore

    private lateinit var mockSetupDataStore: MockSetupDataStore
    private lateinit var viewModel: EditCoverTitleViewModel

    private val uploadCoverTitleException = IllegalStateException("error upload cover title")

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()
        coverConfigStore = CoverConfigStoreImpl()

        productDataStore = ProductDataStoreImpl(dispatcherProvider, mockk())
        coverDataStore = MockCoverDataStore(dispatcherProvider, uploadCoverTitleException)
        broadcastScheduleDataStore = BroadcastScheduleDataStoreImpl(dispatcherProvider, mockk())
        mockSetupDataStore = MockSetupDataStore(productDataStore, coverDataStore, broadcastScheduleDataStore)

        viewModel = EditCoverTitleViewModel(
                hydraConfigStore = HydraConfigStoreImpl(
                        channelConfigStore,
                        ProductConfigStoreImpl(),
                        coverConfigStore,
                        BroadcastScheduleConfigStoreImpl()
                ),
                dispatcher = dispatcherProvider,
                setupDataStore = mockSetupDataStore
        )

        channelConfigStore.setChannelId("12345")
    }

    @Test
    fun `when edit title is success, then it should return success`() {
        coverDataStore.setIsSuccess(true)
        viewModel.editTitle("abc")

        val result = viewModel.observableUpdateTitle.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualTo(NetworkResult.Success(Unit))
    }

    @Test
    fun `when edit title is failed, then it should return failed`() {
        coverDataStore.setIsSuccess(false)
        viewModel.editTitle("abc")

        val result = viewModel.observableUpdateTitle.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualTo(NetworkResult.Fail(uploadCoverTitleException))
    }

    @Test
    fun `when cover is blank, then cover is not valid`() {
        val isValid = viewModel.isValidCoverTitle("")

        Assertions
                .assertThat(isValid)
                .isEqualTo(false)
    }

    @Test
    fun `when cover exceeds maximum characters, then cover is not valid`() {
        coverConfigStore.setMaxTitleChars(5)
        val isValid = viewModel.isValidCoverTitle("Barang Murah")

        Assertions
                .assertThat(isValid)
                .isEqualTo(false)
    }

    @Test
    fun `when cover is not blank and does not exceed maximum characters, then cover is valid`() {
        coverConfigStore.setMaxTitleChars(20)
        val isValid = viewModel.isValidCoverTitle("Barang Murah")

        Assertions
                .assertThat(isValid)
                .isEqualTo(true)
    }

    @Test
    fun `when title is set or edited, then it should return the newest set title`() {
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