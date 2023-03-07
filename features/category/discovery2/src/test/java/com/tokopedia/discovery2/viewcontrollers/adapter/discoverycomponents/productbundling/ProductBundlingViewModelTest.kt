package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productbundling

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.productbundlingusecase.ProductBundlingUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductBundlingViewModelTest {

    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ProductBundlingViewModel =
        spyk(ProductBundlingViewModel(application, componentsItem, 99))

    private val productBundlingUseCase: ProductBundlingUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test for useCase`() {
        val viewModel: ProductBundlingViewModel =
                spyk(ProductBundlingViewModel(application, componentsItem, 99))

        val productBundlingUseCase = mockk<ProductBundlingUseCase>()
        viewModel.productBundlingUseCase = productBundlingUseCase

        assert(viewModel.productBundlingUseCase === productBundlingUseCase)
    }

    @Test
    fun `test for dispatchers useCase`() {
        val viewModel: ProductBundlingViewModel =
            spyk(ProductBundlingViewModel(application, componentsItem, 99))

        val dispacherUseCase = mockk<CoroutineDispatchers>()
        viewModel.coroutineDispatchers = dispacherUseCase

        assert(viewModel.coroutineDispatchers === dispacherUseCase)
    }

    /****************************************** test for component ****************************************/
    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    /****************************************** test for position ****************************************/
    @Test
    fun `test for component position`(){
        TestCase.assertEquals(viewModel.position, 99)
    }

    /****************************************** test for onAttachToViewHolder ****************************************/

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns error`() {
        viewModel.productBundlingUseCase = productBundlingUseCase
        coEvery {
            productBundlingUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.showErrorState().value, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns true and empty list`() {
        viewModel.productBundlingUseCase = productBundlingUseCase
        coEvery {
            productBundlingUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getBundledProductDataList().value == null, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns false`() {
        viewModel.productBundlingUseCase = productBundlingUseCase
        coEvery {
            productBundlingUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getBundledProductDataList().value == null, true)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns true with list`() {
        mockkStatic(Dispatchers::class)
        every { Dispatchers.Default } returns TestCoroutineDispatcher()
        viewModel.productBundlingUseCase = productBundlingUseCase
        viewModel.coroutineDispatchers = rule1.dispatchers
        val list = ArrayList<DataItem>()
        val item = DataItem(name = "product_bundling")
        list.add(item)
        every { componentsItem.data } returns list
        coEvery {
            productBundlingUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getBundledProductDataList().value != null, true)
        unmockkStatic(Dispatchers::class)
    }

    @Test
    fun `test for onAttachToViewHolder when loadFirstPageComponents returns true with empty list`() {
        viewModel.productBundlingUseCase = productBundlingUseCase
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        coEvery {
            productBundlingUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getEmptyBundleData().value, true)
    }
}
