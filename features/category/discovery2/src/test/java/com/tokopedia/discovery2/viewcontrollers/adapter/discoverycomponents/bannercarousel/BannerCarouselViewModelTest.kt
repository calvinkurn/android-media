package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BannerCarouselViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val list = ArrayList<DataItem>()
    private val mockedDataItem:DataItem = mockk(relaxed = true)
    private val dataList = ArrayList<DataItem>().apply {
        add(DataItem())
    }

    private val viewModel: BannerCarouselViewModel by lazy {
        spyk(BannerCarouselViewModel(application, componentsItem, 0))
    }

    private val bannerUseCase: BannerUseCase by lazy {
        mockk()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @Test
    fun `test for fetchBannerData`(){
        viewModel.bannerUseCase = bannerUseCase
        every { componentsItem.properties?.dynamic } returns true
        runBlocking {
            coEvery {
                bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)} throws Exception("Error")
            viewModel.onAttachToViewHolder()
            TestCase.assertEquals(viewModel.hideShimmer.value ,true)
            TestCase.assertEquals(viewModel.getTitleLiveData().value, "")

            coEvery {
                bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)} returns true
            list.clear()
            list.add(mockedDataItem)
            every { componentsItem.data } returns list
            every { componentsItem.properties?.bannerTitle } returns "title"
            viewModel.onAttachToViewHolder()
            TestCase.assertEquals(viewModel.getComponents().value != null, true)
        }
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponents().value == componentsItem)
    }

    @Test
    fun `test for useCase`() {
        val viewModel: BannerCarouselViewModel =
                spyk(BannerCarouselViewModel(application, componentsItem, 99))

        val bannerUseCase = mockk<BannerUseCase>()
        viewModel.bannerUseCase = bannerUseCase
        assert(viewModel.bannerUseCase === bannerUseCase)
    }

    @Test
    fun `title value`() {
        mockkObject(DiscoveryDataMapper)
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns ArrayList()
        every { componentsItem.data } returns null
        var viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTitleLiveData().value == "")
        every { componentsItem.data } returns ArrayList()
        every { componentsItem.properties?.bannerTitle } returns null
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTitleLiveData().value == "")
        every { componentsItem.data } returns dataList
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTitleLiveData().value == "")
        every { componentsItem.properties?.bannerTitle } returns "testTitle"
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        assert(viewModelTest.getTitleLiveData().value == "testTitle")
    }

    @Test
    fun `test lihat url`() {
        every { componentsItem.properties } returns null
        assert(viewModel.getLihatUrl() == "")
        every { componentsItem.properties?.ctaApp } returns null
        assert(viewModel.getLihatUrl() == "")
        every { componentsItem.properties?.ctaApp } returns "testUrl"
        assert(viewModel.getLihatUrl() == "testUrl")
    }

    @Test
    fun `test for carousel Banner List`() {
        var viewModelTest = viewModel
        mockkObject(DiscoveryDataMapper)
        val list: ArrayList<ComponentsItem> = ArrayList()
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns null
        viewModelTest.getComponentData()
        verify(inverse = true) { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        every { componentsItem.data } returns dataList
        viewModelTest = spyk(BannerCarouselViewModel(application, componentsItem, 0))
        verify { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        assert(viewModelTest.getComponentData().value == list)
        unmockkObject(DiscoveryDataMapper)

    }

    @Test
    fun `shouldShowShimmer test`() {
        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 0
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns true
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns true
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties?.dynamic } returns false
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

        every { componentsItem.noOfPagesLoaded } returns 1
        every { componentsItem.properties } returns null
        every { componentsItem.verticalProductFailState } returns false
        assert(!viewModel.shouldShowShimmer())

    }


}