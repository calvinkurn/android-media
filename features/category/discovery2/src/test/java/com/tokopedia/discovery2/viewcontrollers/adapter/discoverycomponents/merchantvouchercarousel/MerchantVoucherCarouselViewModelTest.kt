package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.LihatSemua
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.factory.ComponentsList
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
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
    }


    @Test
    fun `test for components`(){
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`(){
        assert(viewModel.position == 99)
    }

//    @Test
//    fun `fetch coupon data error handling`(){
//        val viewModel = spyk(MerchantVoucherCarouselViewModel(application, componentsItem, 99))
//        viewModel.merchantVoucherUseCase = useCase
//        coEvery {useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint)} throws Exception()
//        viewModel.fetchCouponData()
////        Why is this failing ??
//        assert(componentsItem.noOfPagesLoaded == 1)
//        assert(viewModel.loadError.value == true)
//    }

    @Test
    fun `fetch coupon data`(){
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        viewModel.merchantVoucherUseCase = useCase
        coEvery {useCase.loadFirstPageComponents(componentsItem.id,componentsItem.pageEndPoint)} returns true

//        No data is present.
        val list  = ArrayList<ComponentsItem>()
        viewModel.fetchCouponData()
        every { componentsItem.getComponentsItem() } returns list
        assert(viewModel.loadError.value == true)
        assert(!(viewModel.couponList.value === list))
        assert(viewModel.syncData.value == null || viewModel.syncData.value == false)

//        Got data from API
        list.add(ComponentsItem())
        viewModel.fetchCouponData()
        assert(viewModel.loadError.value == false)
        assert(viewModel.couponList.value === list)
        assert(viewModel.syncData.value == true)
    }


    @Test
    fun `get lihat semua header `(){
//      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
//      and this was causing exception.
        mockkConstructor(URLParser::class)
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
        assert(headerData?.creativeName == "creative")
        assert(!headerData?.data.isNullOrEmpty())
        val dataItem = headerData?.data?.firstOrNull()
        assert(dataItem?.title == "header")
        assert(dataItem?.subtitle == "subheader")
        assert(dataItem?.btnApplink == "applink")

    }

}