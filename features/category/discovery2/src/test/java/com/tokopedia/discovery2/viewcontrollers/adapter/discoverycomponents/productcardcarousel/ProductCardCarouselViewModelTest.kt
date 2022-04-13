package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.user.session.UserSession
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductCardCarouselViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ProductCardCarouselViewModel =
        spyk(ProductCardCarouselViewModel(application, componentsItem, 99))
    private var context: Context = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
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
        //      mocking URL Parser because ComponentItem constructs an object of SearchParameter which uses URLParser
        //      and this was causing exception.
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()

        every { componentsItem.pageEndPoint } returns "abc"
        every { componentsItem.id } returns "101"
        val error = viewModel.getErrorStateComponent()
        assert(error.name == ComponentNames.ProductListEmptyState.componentName)
        assert(error.id == ComponentNames.ProductListEmptyState.componentName)
        assert(error.pageEndPoint == "abc")
        assert(error.parentComponentId == "101")
    }

}