package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.data.type.OverwriteMode
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

    class MockSetupDataStore(
            private val mProductDataStore: ProductDataStore,
            private val mCoverDataStore: CoverDataStore
    ) : PlayBroadcastSetupDataStore, ProductDataStore by mProductDataStore, CoverDataStore by mCoverDataStore {

        var isOverwritten: Boolean = false

        override fun overwrite(dataStore: PlayBroadcastSetupDataStore, modeExclusion: List<OverwriteMode>) {
            isOverwritten = true
        }

        override fun getProductDataStore(): ProductDataStore {
            return mProductDataStore
        }

        override fun getCoverDataStore(): CoverDataStore {
            return mCoverDataStore
        }
    }

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val dispatcherProvider = TestCoroutineDispatcherProvider(testDispatcher)

    private lateinit var mockProductDataStore: ProductDataStore
    private lateinit var mockCoverDataStore: CoverDataStore

    private lateinit var setupDataStore: MockSetupDataStore
    private lateinit var viewModel: DataStoreViewModel

    @Before
    fun setUp() {
        mockProductDataStore = ProductDataStoreImpl(dispatcherProvider, mockk())
        mockCoverDataStore = CoverDataStoreImpl(dispatcherProvider, mockk(), mockk())
        setupDataStore = MockSetupDataStore(mockProductDataStore, mockCoverDataStore)
        viewModel = DataStoreViewModel(setupDataStore)
    }

    @Test
    fun `test get data store`() {
        Assertions
                .assertThat(viewModel.getDataStore())
                .isEqualTo(setupDataStore)
    }

    @Test
    fun `test overwrite data store`() {
        Assertions
                .assertThat(setupDataStore.isOverwritten)
                .isEqualTo(false)

        viewModel.setDataStore(setupDataStore)

        Assertions
                .assertThat(setupDataStore.isOverwritten)
                .isEqualTo(true)
    }
}