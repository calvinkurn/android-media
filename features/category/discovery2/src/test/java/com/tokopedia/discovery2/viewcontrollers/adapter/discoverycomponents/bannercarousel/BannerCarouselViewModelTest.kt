package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.bannerusecase.BannerUseCase
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
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
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class BannerCarouselViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val list = ArrayList<DataItem>()
    private val mockedDataItem:DataItem = mockk(relaxed = true)
    private val mockedDiscoDataMapper :DiscoveryDataMapper = mockk(relaxed = true)
    var context: Context = mockk(relaxed = true)
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

    /**************************** fetchBannerData() *******************************************/

    @Test
    fun `test for fetchBannerData error case`() {
        viewModel.bannerUseCase = bannerUseCase
        every { componentsItem.properties?.dynamic } returns true
        coEvery {
            bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideShimmer.value, true)

    }

    @Test
    fun `test for fetchBannerData UnknownHostException error case`() {
        viewModel.bannerUseCase = bannerUseCase
        every { componentsItem.properties?.dynamic } returns true
        coEvery {
            bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws UnknownHostException("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.showErrorState.value, true)

    }

    @Test
    fun `test for fetchBannerData SocketTimeoutException error case`() {
        viewModel.bannerUseCase = bannerUseCase
        every { componentsItem.properties?.dynamic } returns true
        coEvery {
            bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws SocketTimeoutException("Error")

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.showErrorState.value, true)

    }

    @Test
    fun `test for fetchBannerData success when componentList is not empty`() {
        viewModel.bannerUseCase = bannerUseCase
        every { componentsItem.properties?.dynamic } returns true
        coEvery {
            bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        list.clear()
        list.add(mockedDataItem)
        every { componentsItem.data } returns list
        every { mockedDiscoDataMapper.mapListToComponentList(any(),any(),any(),any()) } returns ArrayList()

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getComponents().value != null, true)
    }

    @Test
    fun `test for fetchBannerData success when componentList is empty`() {
        viewModel.bannerUseCase = bannerUseCase
        mockkObject(DiscoveryDataMapper)
        every { componentsItem.properties?.dynamic } returns true
        coEvery {
            bannerUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        list.clear()
        every { componentsItem.data } returns list
        every { DiscoveryDataMapper.mapListToComponentList(any(),any(),any(),any()) } returns ArrayList()

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.hideShimmer.value , true)
    }

    /**************************** fetchBannerData() *******************************************/

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponents().value == componentsItem)
    }

    /**************************** bannerUseCase test *******************************************/
    @Test
    fun `test for useCase`() {
        val viewModel: BannerCarouselViewModel =
                spyk(BannerCarouselViewModel(application, componentsItem, 99))

        val bannerUseCase = mockk<BannerUseCase>()
        viewModel.bannerUseCase = bannerUseCase

        assert(viewModel.bannerUseCase === bannerUseCase)
    }

    /**************************** end bannerUseCase test *******************************************/

    /**************************** reload() *******************************************/
    @Test
    fun `test for reload`() {
        viewModel.reload()

        assert(componentsItem.noOfPagesLoaded == 0)
    }
    /**************************** end reload() *******************************************/

    /**************************** getTitleLiveData() *******************************************/

    @Test
    fun `title value when banner title is null`() {
        every { componentsItem.data } returns ArrayList()
        every { componentsItem.properties?.bannerTitle } returns null
        val viewModelTest: BannerCarouselViewModel = spyk(BannerCarouselViewModel(application, componentsItem, 0))

        assert(viewModelTest.getTitleLiveData().value == "")

    }


    @Test
    fun `title value when bannerTitle is not null`() {
        every { componentsItem.properties?.bannerTitle } returns "testTitle"

        assert(viewModel.getTitleLiveData().value == "testTitle")
    }

    /**************************** end getTitleLiveData() *******************************************/


    /**************************** getLihatUrl() *******************************************/
    @Test
    fun `test lihat url when properties is null`() {
        every { componentsItem.properties } returns null

        assert(viewModel.getLihatUrl() == "")
    }

    @Test
    fun `test lihat url when ctaApp is null`() {
        every { componentsItem.properties?.ctaApp } returns null

        assert(viewModel.getLihatUrl() == "")
    }

    @Test
    fun `test lihat url when ctaApp is not null`() {
        every { componentsItem.properties?.ctaApp } returns "testUrl"

        assert(viewModel.getLihatUrl() == "testUrl")
    }

    /**************************** end getLihatUrl() *******************************************/

    /**************************** init viewModel *******************************************/
    @Test
    fun `test for carousel Banner List aka init viewModel when componentsItem data is null`() {
        var viewModelTest = viewModel
        val list: ArrayList<ComponentsItem> = ArrayList()
        every { mockedDiscoDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns list
        every { componentsItem.data } returns null
        viewModelTest.getComponentData()

        verify(inverse = true) { mockedDiscoDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }

    }

    @Test
    fun `test for carousel Banner List aka init viewModel when componentsItem data is not null`() {
        mockkObject(DiscoveryDataMapper)
        every { componentsItem.data } returns dataList
        every { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) } returns ArrayList()

        assert(viewModel.getComponentData().value == list)

        verify { DiscoveryDataMapper.mapListToComponentList(any(), any(), any(), any(), any()) }
        unmockkObject(DiscoveryDataMapper)
    }

    /**************************** end init viewModel *******************************************/

    /**************************** shouldShowShimmer() (multiple cases)*******************************************/
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

    @Test
    fun `test for checkForDarkMode`() {
        spyk(context.isDarkMode())

        viewModel.checkForDarkMode(context)

        verify { context.isDarkMode() }
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
    }


}
