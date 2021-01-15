package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play.broadcaster.view.viewmodel.DataStoreViewModel
import com.tokopedia.play.broadcaster.util.TestCoroutineDispatcherProvider
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 24/09/20
 */
class DataStoreViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var productDataStore: ProductDataStore
    private lateinit var coverDataStore: CoverDataStore
    private lateinit var broadcastScheduleDataStore: BroadcastScheduleDataStore

    private lateinit var mockSetupDataStore: MockSetupDataStore
    private lateinit var viewModel: DataStoreViewModel

    @Before
    fun setUp() {
        productDataStore = ProductDataStoreImpl(dispatcherProvider, mockk())
        coverDataStore = CoverDataStoreImpl(dispatcherProvider, mockk(), mockk())
        broadcastScheduleDataStore = BroadcastScheduleDataStoreImpl(dispatcherProvider, mockk())
        mockSetupDataStore = MockSetupDataStore(productDataStore, coverDataStore, broadcastScheduleDataStore)
        viewModel = DataStoreViewModel(mockSetupDataStore)
    }

    @Test
    fun `when get setup data store, then it should return the set setup data store`() {
        Assertions
                .assertThat(viewModel.getDataStore())
                .isEqualTo(mockSetupDataStore)
    }

    @Test
    fun `when overwrite setup data store, then the set setup data store should be overwritten`() {
        Assertions
                .assertThat(mockSetupDataStore.isOverwritten)
                .isEqualTo(false)

        viewModel.setDataStore(mockSetupDataStore)

        Assertions
                .assertThat(mockSetupDataStore.isOverwritten)
                .isEqualTo(true)
    }
}