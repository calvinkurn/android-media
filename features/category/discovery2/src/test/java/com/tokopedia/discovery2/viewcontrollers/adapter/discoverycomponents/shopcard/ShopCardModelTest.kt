package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.shopcardusecase.ShopCardUseCase
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

class ShopCardModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ShopCardViewModel =
        spyk(ShopCardViewModel(application, componentsItem, 99))

    private val shopCardUseCase: ShopCardUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
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
    fun `test for useCase`() {
        val viewModel: ShopCardViewModel =
                spyk(ShopCardViewModel(application, componentsItem, 99))

        val shopCardUseCase = mockk<ShopCardUseCase>()
        viewModel.shopCardUseCase = shopCardUseCase
        assert(viewModel.shopCardUseCase === shopCardUseCase)
    }

    @Test
    fun `test for refreshProductCarouselError`() {

        viewModel.refreshProductCarouselError()
        coVerify { viewModel.refreshProductCarouselError() }

        val list = ArrayList<ComponentsItem>()
        every { viewModel.getShopList() } returns list

        TestCase.assertEquals(viewModel.isLoadingData(), false)
    }

    @Test
    fun `is last page for pagination`(){
        every { componentsItem.nextPageKey } returns "p"
        assert(!viewModel.isLastPage())
        every { componentsItem.nextPageKey } returns ""
        assert(viewModel.isLastPage())
        every { componentsItem.nextPageKey } returns null
        assert(viewModel.isLastPage())
    }

    @Test
    fun `test for handleErrorState`(){
        every { componentsItem.verticalProductFailState } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getShopLoadState().value, true)
    }

    @Test
    fun `test for fetchShopCardData`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")
        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.hideShimmer().value, true)
        TestCase.assertEquals(viewModel.getShopLoadState().value, true)

        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.getShopList() != null, true)
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

    @Test
    fun `test for fetchShopCardPaginatedData`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")
        viewModel.fetchShopCardPaginatedData()
        coVerify { shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint) }
        TestCase.assertEquals(viewModel.getShopList() != null, true)
        TestCase.assertEquals(viewModel.isLoadingData(), false)
        every { viewModel.getShopList() } returns null
        viewModel.fetchShopCardPaginatedData()
        TestCase.assertEquals(viewModel.isLoadingData(), true)


        coEvery {
            shopCardUseCase.getShopCardPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        viewModel.fetchShopCardPaginatedData()
        val list = ArrayList<ComponentsItem>()
        every { viewModel.getShopList() } returns list
        viewModel.fetchShopCardPaginatedData()
        TestCase.assertEquals(viewModel.isLoadingData(), false)
        every { viewModel.getShopList() } returns null
        viewModel.fetchShopCardPaginatedData()
        TestCase.assertEquals(viewModel.isLoadingData(), true)


        coEvery {
            shopCardUseCase.getShopCardPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } returns false
        viewModel.fetchShopCardPaginatedData()
        TestCase.assertEquals(viewModel.getShopList() == null, true)
        every { viewModel.getShopList() } returns null
        viewModel.fetchShopCardPaginatedData()
        TestCase.assertEquals(viewModel.isLoadingData(), true)
    }

    @Test
    fun `test for paginatedErrorData`(){
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem

        TestCase.assertEquals(viewModel.getShopList(), componentItem)
    }

    @Test
    fun `test for getShopCardList`(){
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem

        TestCase.assertEquals(viewModel.getShopList(), componentItem)
    }

    @Test
    fun `test for resetComponent`(){
        viewModel.resetComponent()

        TestCase.assertEquals(viewModel.components.noOfPagesLoaded, 0)
    }

    @Test
    fun `test for component passed to VM`(){
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

}