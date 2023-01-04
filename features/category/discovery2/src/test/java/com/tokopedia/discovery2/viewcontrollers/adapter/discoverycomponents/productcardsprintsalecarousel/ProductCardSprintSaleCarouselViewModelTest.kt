package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.user.session.UserSession
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductCardSprintSaleCarouselViewModelTest{
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: ProductCardSprintSaleCarouselViewModel by lazy {
        spyk(ProductCardSprintSaleCarouselViewModel(application, componentsItem, 99))
    }
    private var context:Context = mockk()

    private val productCardsUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkConstructor(UserSession::class)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        every { application.applicationContext } returns context
    }

    @Test
    fun `test for useCase`() {
        val viewModel: ProductCardSprintSaleCarouselViewModel =
            spyk(ProductCardSprintSaleCarouselViewModel(application, componentsItem, 99))

        val productCardsUseCase = mockk<ProductCardsUseCase>()
        viewModel.productCardsUseCase = productCardsUseCase

        assert(viewModel.productCardsUseCase === productCardsUseCase)
    }

    /**************************** test for Login *******************************************/
    @Test
    fun `isUser Logged in when isLoggedIn is false`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns false

        assert(!viewModel.isUserLoggedIn())
    }
    @Test
    fun `isUser Logged in when isLoggedIn is true`() {
        every { constructedWith<UserSession>(OfTypeMatcher<Context>(Context::class)).isLoggedIn } returns true

        assert(viewModel.isUserLoggedIn())
    }
    /**************************** end of Login *******************************************/

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun `onAttachToViewHolder when getComponentsItem is not null`() {
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getProductCarouselItemsListData().value != null, true)
    }

    @Test
    fun `onAttachToViewHolder when getComponentsItem is null`() {
        every { componentsItem.getComponentsItem() } returns null

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getProductCarouselItemsListData().value == null, true)
    }
    /**************************** onAttachToViewHolder() *******************************************/

    /**************************** fetchProductCarouselData() *******************************************/

    @Test
    fun `fetchProductCarouselData when loadFirstPageComponents returns error`() {
        viewModel.productCardsUseCase = productCardsUseCase
        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(),any())
        } throws Exception("Error")

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.syncData.value == null, true)
    }

    @Test
    fun `fetchProductCarouselData when loadFirstPageComponents is true`() {
        viewModel.productCardsUseCase = productCardsUseCase
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem
        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(),any())
        } returns true

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getProductCarouselItemsListData().value != null, true)
        assertEquals(viewModel.syncData.value != null, true)
    }

    @Test
    fun `fetchProductCarouselData when loadFirstPageComponents is false`() {
        viewModel.productCardsUseCase = productCardsUseCase
        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(),any())
        } returns false

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.syncData.value == null, true)
    }
    /**************************** fetchProductCarouselData() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(UserSession::class)
        unmockkConstructor(URLParser::class)
        unmockkStatic(::getComponent)
    }
}
