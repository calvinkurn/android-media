package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class ErrorLoadViewModelTest {
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: ErrorLoadViewModel by lazy {
        spyk(ErrorLoadViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** reloadComponentData() *******************************************/

    @Test
    fun reloadComponentData() {
        runBlocking {
            val list = ArrayList<DataItem>()
            list.add(mockk(relaxed = true))
            coEvery { componentsItem.data } returns list
            coEvery { componentsItem.parentComponentId } returns ""
            coEvery { componentsItem.pageEndPoint } returns ""
            mockkStatic(::getComponent)
            coEvery { getComponent(any(), any()) } returns componentsItem

            coEvery { componentsItem.noOfPagesLoaded } returns 0
            coEvery { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
            coEvery { viewModel.merchantVoucherUseCase.loadFirstPageComponents(any(), any()) } returns true

            viewModel.reloadComponentData()

            Assert.assertEquals(viewModel.syncData.value, true)

            coEvery { componentsItem.noOfPagesLoaded } returns 0
            coEvery { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
            coEvery { viewModel.productCardUseCase.loadFirstPageComponents(any(), any()) } returns true

            viewModel.reloadComponentData()

            Assert.assertEquals(viewModel.syncData.value, true)

            coEvery { componentsItem.noOfPagesLoaded } returns 1
            coEvery { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
            coEvery { viewModel.merchantVoucherUseCase.getVoucherUseCase(any(), any()) } returns true

            viewModel.reloadComponentData()

            Assert.assertEquals(viewModel.syncData.value, true)

            coEvery { componentsItem.noOfPagesLoaded } returns 1
            coEvery { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
            coEvery { viewModel.productCardUseCase.getProductCardsUseCase(any(), any()) } returns true

            viewModel.reloadComponentData()

            Assert.assertEquals(viewModel.syncData.value, true)
        }

    }
    /**************************** reloadComponentData() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}