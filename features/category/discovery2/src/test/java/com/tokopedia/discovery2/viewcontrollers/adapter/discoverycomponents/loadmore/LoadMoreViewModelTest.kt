package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.loadmore

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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
    var context: Context = mockk(relaxed = true)
    private var viewModel: LoadMoreViewModel =
            spyk(LoadMoreViewModel(application, componentsItem, 99))

    private val productCardUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    private val merchantVoucherUseCase: MerchantVoucherUseCase by lazy {
        mockk()
    }

    private val bannerInfiniteUseCase: BannerInfiniteUseCase by lazy {
        mockk()
    }

    private val shopCardInfiniteUseCase: ShopCardUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        every { componentsItem.data } returns null
        val list = ArrayList<DataItem>()
        list.add(mockk(relaxed = true))
        coEvery { componentsItem.data } returns list
        coEvery { componentsItem.id } returns ""
        coEvery { componentsItem.parentComponentId } returns ""
        coEvery { componentsItem.pageEndPoint } returns ""
        coEvery { getComponent(any(), any()) } returns componentsItem
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()

        unmockkStatic(::getComponent)
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

    /**************************** test getViewOrientation() *******************************************/
    @Test
    fun `get Component Orientation when loadForHorizontal is true`() {
        every { componentsItem.loadForHorizontal } returns true

        assert(viewModel.getViewOrientation())
    }

    @Test
    fun `get Component Orientation when loadForHorizontal is false`() {
        every { componentsItem.loadForHorizontal } returns false

        assert(!viewModel.getViewOrientation())
    }
    /**************************** end of getViewOrientation() *******************************************/

    /**************************** test onAttachViewHolder() *******************************************/
    @Test
    fun `test for onAttachViewHolder for when MerchantVoucherList when getPaginatedData returns error`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

//        verify { getComponent(componentsItem.id, componentsItem.pageEndPoint) }
        TestCase.assertEquals(viewModel.syncData.value, true)
    }
    @Test
    fun `test for onAttachViewHolder for when MerchantVoucherList when getPaginatedData returns true`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        viewModel.onAttachToViewHolder()
        TestCase.assertEquals(viewModel.syncData.value, true)
    }
    @Test
    fun `test for onAttachViewHolder for when MerchantVoucherList when getPaginatedData returns false`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns false
        viewModel.onAttachToViewHolder()
        TestCase.assertEquals(viewModel.syncData.value, false)
    }

    @Test
    fun `test for onAttachViewHolder for when MerchantVoucherList when getPaginatedData returns true and loadForHorizontal is true`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        every { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        every { componentsItem.loadForHorizontal } returns true
        coEvery {
            merchantVoucherUseCase.getPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        verify { viewModel.getViewOrientation() }
    }

    @Test
    fun `test for onAttachViewHolder for when ProductCardCarousel when getProductCardsUseCase returns error`() {
        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getProductCardsUseCase(
                    componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

//        verify { getComponent(componentsItem.id, componentsItem.pageEndPoint) }
        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachViewHolder for when ProductCardCarousel when getProductCardsUseCase returns true`() {
        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getProductCardsUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachViewHolder for when ProductCardCarousel when getProductCardsUseCase returns false`() {
        viewModel.productCardUseCase = productCardUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery {
            productCardUseCase.getProductCardsUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, false)

    }

    @Test
    fun `test for onAttachViewHolder for when BannerInfinite when getBannerUseCase returns error`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.BannerInfinite.componentName
        coEvery {
            bannerInfiniteUseCase.getBannerUseCase(
                    componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachViewHolder for when BannerInfinite when getBannerUseCase returns true`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.BannerInfinite.componentName
        coEvery {
            bannerInfiniteUseCase.getBannerUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachViewHolder for when BannerInfinite when getBannerUseCase returns false`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.BannerInfinite.componentName
        coEvery {
            bannerInfiniteUseCase.getBannerUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, false)

    }

    @Test
    fun `test for onAttachViewHolder for when BannerInfinite when getShopCardUseCase returns error`() {
        viewModel.shopCardInfiniteUseCase = shopCardInfiniteUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ShopCardInfinite.componentName
        coEvery {
            shopCardInfiniteUseCase.getShopCardUseCase(
                    componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachViewHolder for when BannerInfinite when getShopCardUseCase returns true`() {
        viewModel.shopCardInfiniteUseCase = shopCardInfiniteUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ShopCardInfinite.componentName
        coEvery {
            shopCardInfiniteUseCase.getShopCardUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for onAttachViewHolder for when BannerInfinite when getShopCardUseCase returns false`() {
        viewModel.shopCardInfiniteUseCase = shopCardInfiniteUseCase
        every { componentsItem.loadForHorizontal } returns false
        every { componentsItem.parentComponentName } returns ComponentNames.ShopCardInfinite.componentName
        coEvery {
            shopCardInfiniteUseCase.getShopCardUseCase(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.syncData.value, false)

    }

    /**************************** end of onAttachViewHolder() *******************************************/

    @Test
    fun `test for checkForDarkMode`() {
        spyk(context.isDarkMode())

        viewModel.checkForDarkMode(context)

        verify { viewModel.checkForDarkMode(context) }
    }

}
