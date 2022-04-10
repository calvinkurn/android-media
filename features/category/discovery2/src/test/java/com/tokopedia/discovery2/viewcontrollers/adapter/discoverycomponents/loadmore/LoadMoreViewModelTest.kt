package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class LoadMoreViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: LoadMoreViewModel =
            spyk(LoadMoreViewModel(application, componentsItem, 99))

    private val productCardUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    private val merchantVoucherUseCase: MerchantVoucherUseCase by lazy {
        mockk()
    }

    @Before
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
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun `test for productCardUseCase`() {
        val viewModel: LoadMoreViewModel =
                spyk(LoadMoreViewModel(application, componentsItem, 99))

        val productCardUseCase = mockk<ProductCardsUseCase>()
        viewModel.productCardUseCase = productCardUseCase
        assert(viewModel.productCardUseCase === productCardUseCase)
    }

    @Test
    fun `test for merchantVoucherUseCase`() {
        val viewModel: LoadMoreViewModel =
                spyk(LoadMoreViewModel(application, componentsItem, 99))

        val merchantVoucherUseCase = mockk<MerchantVoucherUseCase>()
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        assert(viewModel.merchantVoucherUseCase === merchantVoucherUseCase)
    }

    @Test
    fun `get Component Orientation`() {
        every { componentsItem.loadForHorizontal } returns true
        assert(viewModel.getViewOrientation())

        every { componentsItem.loadForHorizontal } returns false
        assert(!viewModel.getViewOrientation())
    }

    @Test
    fun `test for onAttachViewHolder`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName

        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")
        viewModel.onAttachToViewHolder()
        verify { getComponent(componentsItem.id, componentsItem.pageEndPoint) }
        TestCase.assertEquals(viewModel.syncData.value, true)

        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        viewModel.onAttachToViewHolder()
        TestCase.assertEquals(viewModel.syncData.value, true)

        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns false
        viewModel.onAttachToViewHolder()
        TestCase.assertEquals(viewModel.syncData.value, false)

        every { componentsItem.loadForHorizontal } returns true
        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        viewModel.onAttachToViewHolder()
        verify { viewModel.getViewOrientation() }


        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getProductCardsUseCase(
                    componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")
        viewModel.onAttachToViewHolder()
        verify { getComponent(componentsItem.id, componentsItem.pageEndPoint) }
        TestCase.assertEquals(viewModel.syncData.value, true)

        coEvery {
            productCardUseCase.getProductCardsUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        viewModel.onAttachToViewHolder()
        TestCase.assertEquals(viewModel.syncData.value, true)

        coEvery {
            productCardUseCase.getProductCardsUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns false
        viewModel.onAttachToViewHolder()
        TestCase.assertEquals(viewModel.syncData.value, false)

    }

}