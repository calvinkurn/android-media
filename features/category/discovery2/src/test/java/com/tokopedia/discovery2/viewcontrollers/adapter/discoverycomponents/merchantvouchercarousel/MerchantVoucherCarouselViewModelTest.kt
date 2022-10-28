package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.LihatSemua
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MerchantVoucherCarouselViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel:MerchantVoucherCarouselViewModel = spyk(MerchantVoucherCarouselViewModel(application, componentsItem, 99))
    private val useCase: MerchantVoucherUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        mockkConstructor(URLParser::class)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
    }


    @Test
    fun `test for components`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

    /**************************** test for fetchCouponData() *******************************************/
    @Test
    fun `fetch coupon data error handling`(){
        val viewModel = spyk(MerchantVoucherCarouselViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        coEvery {useCase.loadFirstPageComponents(any(),any())} throws Exception()
        viewModel.fetchCouponData()
        assert(viewModel.loadError.value == true)
    }

    @Test
    fun `fetch coupon data when list is empty`() {
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        viewModel.merchantVoucherUseCase = useCase
        coEvery { useCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true

//        No data is present.
        val list = ArrayList<ComponentsItem>()
        viewModel.fetchCouponData()

        every { componentsItem.getComponentsItem() } returns list

        assert(viewModel.loadError.value == true)
    }

    @Test
    fun `fetch coupon data when list is not empty`() {
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        viewModel.merchantVoucherUseCase = useCase
        val list = ArrayList<ComponentsItem>()
        list.add(ComponentsItem())
        every { componentsItem.getComponentsItem() } returns list
        coEvery { useCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint) } returns true

        viewModel.fetchCouponData()

        assert(viewModel.syncData.value == true)
    }

    /**************************** end of fetchCouponData() *******************************************/

    /**************************** test for lihat semua header *******************************************/
    @Test
    fun `get lihat semua header `(){
//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val lihatSemua = mockk<LihatSemua>()
        every { lihatSemua.header } returns "header"
        every { lihatSemua.subheader } returns "subheader"
        every { lihatSemua.applink } returns "applink"
        every { componentsItem.lihatSemua } returns lihatSemua
        every { componentsItem.creativeName } returns "creative"

        viewModel.getLihatSemuaHeader()
        val headerData = viewModel.headerData.value

        assert(headerData?.name == ComponentsList.MerchantVoucherCarousel.componentName)

    }

    /**************************** end of lihat semua header *******************************************/


    @Test
    fun `test for fetchCarouselPaginatedCoupon throws exception test states`(){
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val viewModel = spyk(MerchantVoucherCarouselViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        val list = ArrayList<ComponentsItem>()
        for(i in 1..10)
            list.add(ComponentsItem(id = i.toString()))
        coEvery {
            useCase.getCarouselPaginatedData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } throws Exception("any")
        every { componentsItem.getComponentsItem() } returns list
        viewModel.fetchCarouselPaginatedCoupon()

        assert(!viewModel.isLoadingData())
        assert(componentsItem.horizontalProductFailState)
        assert(viewModel.syncData.value == true)
        assert(viewModel.couponList.value?.size == 11)
        assert(viewModel.couponList.value?.last()?.name == ComponentNames.CarouselErrorLoad.componentName)

    }
    @Test
    fun `test for fetchCarouselPaginatedCoupon throws exception - ErrorLoad Component state`(){
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        componentsItem.pageEndPoint = "test page"
        componentsItem.id = "100"
        componentsItem.name = ComponentNames.MerchantVoucherCarousel.componentName
        componentsItem.position = 15
        val viewModel = spyk(MerchantVoucherCarouselViewModel(application, componentsItem, 99))
        viewModel.merchantVoucherUseCase = useCase
        val list = ArrayList<ComponentsItem>()
        for(i in 1..10)
            list.add(ComponentsItem(id = i.toString()))
        coEvery {
            useCase.getCarouselPaginatedData(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } throws Exception("any")
        every { componentsItem.getComponentsItem() } returns list
        viewModel.fetchCarouselPaginatedCoupon()

        val errorComponent = viewModel.couponList.value?.last()
        assert(errorComponent?.pageEndPoint == "test page")
        assert(errorComponent?.parentComponentId == "100")
        assert(errorComponent?.parentComponentName == ComponentNames.MerchantVoucherCarousel.componentName)
        assert(errorComponent?.id == ComponentNames.CarouselErrorLoad.componentName)
        assert(errorComponent?.parentComponentPosition == 15)

    }

    @Test
    fun `get page size of call`(){
        assert(viewModel.getPageSize() == 10)
    }

    @Test
    fun `is last page for pagination`() {
        every { componentsItem.nextPageKey } returns "p"
        assert(!viewModel.isLastPage())
        every { componentsItem.nextPageKey } returns ""
        assert(viewModel.isLastPage())
        every { componentsItem.nextPageKey } returns null
        assert(viewModel.isLastPage())
    }

    @Test
    fun `is refreshErrorLoad`(){
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val viewModel = spyk(MerchantVoucherCarouselViewModel(application, componentsItem, 99))
        val list = ArrayList<ComponentsItem>()
        for(i in 1..10)
            list.add(ComponentsItem(id = i.toString()))
        every { componentsItem.getComponentsItem() } returns list

        viewModel.refreshProductCarouselError()
        assert(!viewModel.isLoadingData())
        assert(viewModel.couponList.value?.size == 10 )
        assert(viewModel.syncData.value == true)
    }


}
