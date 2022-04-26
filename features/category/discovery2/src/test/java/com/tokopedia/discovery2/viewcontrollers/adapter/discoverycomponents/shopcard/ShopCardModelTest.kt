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

    /**************************** test for refreshProductCarouselError() *******************************************/
    @Test
    fun `test for refreshProductCarouselError`() {
        viewModel.refreshProductCarouselError()
        coVerify { viewModel.refreshProductCarouselError() }

        val list = ArrayList<ComponentsItem>()
        every { viewModel.getShopList() } returns list

        TestCase.assertEquals(viewModel.isLoadingData(), false)
    }

    /**************************** test for pagination() *******************************************/
    @Test
    fun `is last page for pagination when nextPageKey is not empty`() {
        every { componentsItem.nextPageKey } returns "p"

        assert(!viewModel.isLastPage())
    }

    @Test
    fun `is last page for pagination when nextPageKey is empty`() {
        every { componentsItem.nextPageKey } returns ""

        assert(viewModel.isLastPage())
    }

    @Test
    fun `is last page for pagination when nextPageKey is null`() {
        every { componentsItem.nextPageKey } returns null

        assert(viewModel.isLastPage())
    }

    /**************************** end of pagination() *******************************************/

    /**************************** test for handleErrorState() *******************************************/
    @Test
    fun `test for handleErrorState`(){
        every { componentsItem.verticalProductFailState } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getShopLoadState().value, true)
    }

    /**************************** test for fetchShopCardData() *******************************************/
    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents returns error`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.hideShimmer().value, true)
    }
    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents does not returns error`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.getShopList() != null, true)
    }

    /**************************** end of fetchShopCardData() *******************************************/

    /**************************** test for shouldShowShimmer() *******************************************/
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

    /**************************** test for fetchShopCardPaginatedData() *******************************************/

    @Test
    fun `test for fetchShopCardPaginatedData when getShopCardPaginatedData return error`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.isLoadingData(), false)
    }

    @Test
    fun `test for fetchShopCardPaginatedData when getShopCardPaginatedData return error and shopList returns null`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(componentsItem.id, componentsItem.pageEndPoint)
        } throws Exception("Error")
        every { viewModel.getShopList() } returns null

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.isLoadingData(), true)
    }

    @Test
    fun `test for fetchShopCardPaginatedData when getShopCardPaginatedData return true`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        val list = ArrayList<ComponentsItem>()
        every { viewModel.getShopList() } returns list

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.isLoadingData(), false)
    }

    @Test
    fun `test for fetchShopCardPaginatedData when getShopCardPaginatedData return true and shopList is null`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        every { viewModel.getShopList() } returns null

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.isLoadingData(), true)

    }

    @Test
    fun `test for fetchShopCardPaginatedData when getShopCardPaginatedData return false and shopList is null`() {
        viewModel.shopCardUseCase = shopCardUseCase
        every { viewModel.getShopList() } returns null
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.getShopList() == null, true)
    }

    @Test
    fun `test for fetchShopCardPaginatedData when getShopCardPaginatedData return false and shopList is not null`() {
        val list = ArrayList<ComponentsItem>()
        every { viewModel.getShopList() } returns list
        coEvery {
            shopCardUseCase.getShopCardPaginatedData(
                    componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.isLoadingData(), false)
    }

    /**************************** end of fetchShopCardPaginatedData() *******************************************/

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