package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductCardCarouselViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val productCardsUseCase: ProductCardsUseCase = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ProductCardCarouselViewModel =
        spyk(ProductCardCarouselViewModel(application, componentsItem, 99))
    private var context: Context = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
    }


    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application`() {
        assert(viewModel.application === application)
    }

    @Test
    fun `isUser Logged in`() {
        mockkConstructor(UserSession::class)
        every { application.applicationContext } returns context
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false
        assert(!viewModel.isUserLoggedIn())
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true
        assert(viewModel.isUserLoggedIn())
        unmockkConstructor(UserSession::class)
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
    fun `get product list`() {
        every { componentsItem.getComponentsItem() } returns null
        assert(viewModel.getProductList() == null)
        val list: List<ComponentsItem> = mutableListOf()
        every { componentsItem.getComponentsItem() } returns list
        assert(viewModel.getProductList() === list)
    }

    @Test
    fun `get pagination page size for carousel`() {
        assert(viewModel.getPageSize() == 10)
    }

    @Test
    fun `are filters applied`() {
        every { componentsItem.selectedFilters } returns null
        every { componentsItem.selectedSort } returns null
        assert(!viewModel.areFiltersApplied())
        val filterMap = HashMap<String, String>()
        val sortMap = HashMap<String, String>()
        every { componentsItem.selectedFilters } returns filterMap
        assert(!viewModel.areFiltersApplied())
        every { componentsItem.selectedSort } returns sortMap
        assert(!viewModel.areFiltersApplied())
        filterMap["pMax"] = "100"
        assert(viewModel.areFiltersApplied())
        sortMap["ob"] = "23"
        assert(viewModel.areFiltersApplied())
        filterMap.clear()
        assert(viewModel.areFiltersApplied())
    }

    @Test
    fun `test atc failure`() {
        assert(viewModel.atcFailed.value == null)
        viewModel.handleAtcFailed(5)
        assert(viewModel.atcFailed.value == 5)
    }


    @Test
    fun `get error component`() {
        every { componentsItem.pageEndPoint } returns "abc"
        every { componentsItem.id } returns "101"
        val error = viewModel.getErrorStateComponent()
        assert(error.name == ComponentNames.ProductListEmptyState.componentName)
        assert(error.id == ComponentNames.ProductListEmptyState.componentName)
        assert(error.pageEndPoint == "abc")
        assert(error.parentComponentId == "101")
    }

    @Test
    fun `test for refresh Product Carousel Error`() {
        val componentsItem = ComponentsItem(id = "1")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItem)
        every { viewModel.getProductList() } returns list
        viewModel.refreshProductCarouselError()
        assert(viewModel.getProductCarouselItemsListData().value != null)
    }

    @Test
    fun `is loading`() {
        assert(!viewModel.isLoadingData())
    }

    @Test
    fun `test for handle Mix Left Data if null`() {
        every { componentsItem.properties?.mixLeft } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftData().value == null)
    }

    @Test
    fun `test for handle Mix Left Data if non-null`() {
        val mixLeft: MixLeft = mockk(relaxed = true)
        every { componentsItem.properties?.mixLeft } returns mixLeft
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftData().value == mixLeft)
    }

    @Test
    fun `test for handle Error State`() {
        every { componentsItem.verticalProductFailState } returns true
        viewModel.onAttachToViewHolder()
        assert(viewModel.getProductLoadState().value == true)
    }

    @Test
    fun `test for reset Component`() {
        val componentsItemLocal = spyk(ComponentsItem(id = "2"))
         var viewModel: ProductCardCarouselViewModel =
            spyk(ProductCardCarouselViewModel(application, componentsItemLocal, 99))
        componentsItemLocal.noOfPagesLoaded = 5
        componentsItemLocal.pageLoadedCounter = 5
        viewModel.resetComponent()
        assert(componentsItemLocal.noOfPagesLoaded == 0)
        assert(componentsItemLocal.pageLoadedCounter == 1)
    }

    @Test
    fun `test atc failed`() {
        viewModel.handleAtcFailed(11)
        assert(viewModel.atcFailed.value == 11)
    }

    @Test
    fun `test for add MixLeft If Present`() {
        every { componentsItem.properties?.mixLeft?.bannerImageUrlMobile } returns "false"
        val componentsItem = ComponentsItem(id = "1")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItem)
        every { viewModel.getProductList() } returns list
        viewModel.refreshProductCarouselError()
        assert(viewModel.getProductCarouselItemsListData().value?.size == 2)
    }

    @Test
    fun `test for add Error ReLoad View`() {
        val componentsItemNew = ComponentsItem(id = "1")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItemNew)
        every { viewModel.getProductList() } returns list
        every { componentsItem.properties?.mixLeft?.bannerImageUrlMobile } returns "false"
        viewModel.fetchCarouselPaginatedProducts()
        assert(viewModel.getProductCarouselItemsListData().value?.size == 3)
    }

    @Test
    fun `test for add Load More on error`() {
        mockkObject(Utils)
        viewModel.productCardsUseCase = productCardsUseCase
        val componentsItemNew = ComponentsItem(id = "1")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItemNew)

        every { viewModel.getProductList() } returns list
        every { componentsItem.properties?.mixLeft?.bannerImageUrlMobile } returns "false"
        every { Utils.nextPageAvailable(componentsItem, 10) } returns true
        coEvery {
            productCardsUseCase.getCarouselPaginatedData(
                componentsItem.id, componentsItem.pageEndPoint, 10
            )
        } returns true

        viewModel.fetchCarouselPaginatedProducts()

        assert(viewModel.getProductCarouselItemsListData().value?.size == 3)
    }

}
