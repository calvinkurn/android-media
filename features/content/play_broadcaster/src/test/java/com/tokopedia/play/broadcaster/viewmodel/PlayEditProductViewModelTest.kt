package com.tokopedia.play.broadcaster.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStoreImpl
import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStore
import com.tokopedia.play.broadcaster.data.datastore.BroadcastScheduleDataStoreImpl
import com.tokopedia.play.broadcaster.model.ModelBuilder
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockProductDataStore
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlayEditProductViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.event.Event
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 25/09/20
 */
class PlayEditProductViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcherProvider = CoroutineTestDispatchersProvider

    private lateinit var channelConfigStore: ChannelConfigStore

    private lateinit var productDataStore: MockProductDataStore
    private lateinit var coverDataStore: MockCoverDataStore
    private lateinit var broadcastScheduleDataStore: BroadcastScheduleDataStore
    private lateinit var mockSetupDataStore: MockSetupDataStore

    private lateinit var viewModel: PlayEditProductViewModel

    private val modelBuilder = ModelBuilder()

    private val channelId = "12345"

    private val productDataList = List(3) {
        modelBuilder.buildProductData(id = it.toLong() + 1)
    }

    @Before
    fun setUp() {
        channelConfigStore = ChannelConfigStoreImpl()

        productDataStore = MockProductDataStore(dispatcherProvider)
        coverDataStore = MockCoverDataStore(dispatcherProvider)
        broadcastScheduleDataStore = BroadcastScheduleDataStoreImpl(dispatcherProvider, mockk())
        mockSetupDataStore = MockSetupDataStore(productDataStore, coverDataStore, broadcastScheduleDataStore)

        productDataStore.setSelectedProducts(productDataList)

        viewModel = PlayEditProductViewModel(
                channelConfigStore,
                dispatcherProvider,
                mockSetupDataStore
        )

        channelConfigStore.setChannelId(channelId)
    }

    @Test
    fun `when unselect product, then the product should be selected`() {
        Assertions
                .assertThat(productDataStore.isProductSelected(1))
                .isEqualTo(true)

        viewModel.selectProduct(1, false)

        Assertions
                .assertThat(productDataStore.isProductSelected(1))
                .isEqualTo(false)

        Assertions
                .assertThat(productDataStore.selectedProductsId)
                .containsExactlyInAnyOrder(2, 3)

        viewModel.selectProduct(2, false)

        Assertions
                .assertThat(productDataStore.selectedProductsId)
                .containsExactlyInAnyOrder(3)
    }

    @Test
    fun `when un-select product and then select it, then the product should be selected`() {
        viewModel.selectProduct(1, false)

        Assertions
                .assertThat(productDataStore.isProductSelected(1))
                .isEqualTo(false)

        viewModel.selectProduct(1, true)

        Assertions
                .assertThat(productDataStore.isProductSelected(1))
                .isEqualTo(true)

        Assertions
                .assertThat(productDataStore.selectedProductsId)
                .containsExactlyInAnyOrder(1, 2, 3)
    }

    @Test
    fun `when select product that is not selected before, then it should throw exception`() {
        Assertions
                .assertThatIllegalStateException()
                .isThrownBy { viewModel.selectProduct(5, true) }
    }

    @Test
    fun `when upload product success, then it should return success`() {
        productDataStore.setUploadSuccess(true)
        viewModel.uploadProduct()

        val result = viewModel.observableUploadProductEvent.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isEqualToComparingFieldByFieldRecursively(NetworkResult.Success(Event(Unit)))
    }

    @Test
    fun `when upload product failed, then it should return failed`() {
        productDataStore.setUploadSuccess(false)
        viewModel.uploadProduct()

        val result = viewModel.observableUploadProductEvent.getOrAwaitValue()

        Assertions
                .assertThat(result)
                .isInstanceOf(NetworkResult.Fail::class.java)
    }

    @Test
    fun `when get selected products, then it should return the mapped products with the same id and count`() {
        Assertions
                .assertThat(viewModel.selectedProducts)
                .hasSize(3)

        Assertions
                .assertThat(viewModel.selectedProducts.map { it.id })
                .containsExactlyInAnyOrder(1, 2, 3)
    }

    @Test
    fun `when product is selected or unselected, it should update the selected product list`() {
        Assertions
                .assertThat(viewModel.observableSelectedProducts.getOrAwaitValue())
                .hasSize(3)

        viewModel.selectProduct(1, false)

        Assertions
                .assertThat(viewModel.observableSelectedProducts.getOrAwaitValue())
                .hasSize(2)

        viewModel.selectProduct(1, true)

        Assertions
                .assertThat(viewModel.observableSelectedProducts.getOrAwaitValue())
                .hasSize(3)
    }
}