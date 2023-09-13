package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.usecase.bannerinfiniteusecase.BannerInfiniteUseCase
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
import io.mockk.*
import kotlinx.coroutines.Dispatchers
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
    @Throws(Exception::class)
    fun setUp() {
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

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** reloadComponentData() *******************************************/

    @Test
    fun `reloadComponentData when loadFirstPageComponents returns true for MerchantVoucherList and noOfPagesLoaded is 0 and we get data`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 0
        coEvery { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        coEvery { viewModel.merchantVoucherUseCase?.loadFirstPageComponents(any(), any()) } returns true
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        mockkStatic(::getComponent)
        val merchantVoucherComponent: ComponentsItem = spyk()
        coEvery { getComponent(componentsItem.parentComponentId, componentsItem.pageEndPoint) } returns merchantVoucherComponent
        every { merchantVoucherComponent.getComponentsItem() } returns listOf(mockk())
        viewModel.reloadComponentData()
        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when loadFirstPageComponents returns true for MerchantVoucherList and noOfPagesLoaded is 0 and we don't get data`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 0
        coEvery { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        coEvery { viewModel.merchantVoucherUseCase?.loadFirstPageComponents(any(), any()) } returns true
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        mockkStatic(::getComponent)
        val merchantVoucherComponent: ComponentsItem = spyk()
        coEvery { getComponent(componentsItem.parentComponentId, componentsItem.pageEndPoint) } returns merchantVoucherComponent

        every { merchantVoucherComponent.getComponentsItem() } returns null
        viewModel.reloadComponentData()
        Assert.assertEquals(viewModel.syncData.value, false)

        every { merchantVoucherComponent.getComponentsItem() } returns listOf()
        viewModel.reloadComponentData()
        Assert.assertEquals(viewModel.syncData.value, false)
    }

    @Test
    fun `reloadComponentData when merchantVoucherUseCase returns true for merchantVoucherUseCase and noOfPagesLoaded is 1`() {
        viewModel.merchantVoucherUseCase = merchantVoucherUseCase
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        coEvery { componentsItem.noOfPagesLoaded } returns 1
        coEvery { componentsItem.parentComponentName } returns ComponentNames.MerchantVoucherList.componentName
        val childComponentsItem: ComponentsItem = spyk()
        val dataItem: DataItem = spyk()
        val listOfComps = arrayListOf(childComponentsItem)
        val listOfData = arrayListOf(dataItem)
        every { componentsItem.getComponentsItem() } returns listOfComps
        every { childComponentsItem.data } returns listOfData
        coEvery { viewModel.merchantVoucherUseCase?.loadFirstPageComponents(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value == null, true)
    }

    @Test
    fun `reloadComponentData when loadFirstPageComponents returns true for ProductCardCarousel and noOfPagesLoaded is 0`() {
        viewModel.productCardUseCase = productCardUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 0
        coEvery { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery { viewModel.productCardUseCase?.loadFirstPageComponents(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when getProductCardsUseCase returns true for ProductCardCarousel and noOfPagesLoaded is 1`() {
        viewModel.productCardUseCase = productCardUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 1
        coEvery { componentsItem.parentComponentName } returns ComponentNames.ProductCardCarousel.componentName
        coEvery { viewModel.productCardUseCase?.getProductCardsUseCase(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when loadFirstPageComponents returns true for BannerInfinite and noOfPagesLoaded is 0`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 0
        coEvery { componentsItem.parentComponentName } returns ComponentNames.BannerInfinite.componentName
        coEvery { viewModel.bannerInfiniteUseCase?.loadFirstPageComponents(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when getBannerUseCase returns true for BannerInfinite and noOfPagesLoaded is 1`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 1
        coEvery { componentsItem.parentComponentName } returns ComponentNames.BannerInfinite.componentName
        coEvery { viewModel.bannerInfiniteUseCase?.getBannerUseCase(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when getBannerUseCase throws error true for BannerInfinite and noOfPagesLoaded is 1`() {
        viewModel.bannerInfiniteUseCase = bannerInfiniteUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 1
        coEvery { componentsItem.parentComponentName } returns ComponentNames.BannerInfinite.componentName
        coEvery { viewModel.bannerInfiniteUseCase?.getBannerUseCase(any(), any()) } throws Exception("Error")

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.getShowLoaderStatus().value, false)
    }

    @Test
    fun `reloadComponentData when loadFirstPageComponents returns true for ShopCardInfinite and noOfPagesLoaded is 0`() {
        viewModel.shopCardInfiniteUseCase = shopCardInfiniteUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 0
        coEvery { componentsItem.parentComponentName } returns ComponentNames.ShopCardInfinite.componentName
        coEvery { viewModel.shopCardInfiniteUseCase?.loadFirstPageComponents(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when getShopCardUseCase returns true for ShopCardInfinite and noOfPagesLoaded is 1`() {
        viewModel.shopCardInfiniteUseCase = shopCardInfiniteUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 1
        coEvery { componentsItem.parentComponentName } returns ComponentNames.ShopCardInfinite.componentName
        coEvery { viewModel.shopCardInfiniteUseCase?.getShopCardUseCase(any(), any()) } returns true

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `reloadComponentData when getShopCardUseCase throws error true for ShopCardInfinite and noOfPagesLoaded is 1`() {
        viewModel.shopCardInfiniteUseCase = shopCardInfiniteUseCase
        coEvery { componentsItem.noOfPagesLoaded } returns 1
        coEvery { componentsItem.parentComponentName } returns ComponentNames.ShopCardInfinite.componentName
        coEvery { viewModel.shopCardInfiniteUseCase?.getShopCardUseCase(any(), any()) } throws Exception("Error")

        viewModel.reloadComponentData()

        Assert.assertEquals(viewModel.getShowLoaderStatus().value, false)
    }

    /**************************** reloadComponentData() *******************************************/

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
