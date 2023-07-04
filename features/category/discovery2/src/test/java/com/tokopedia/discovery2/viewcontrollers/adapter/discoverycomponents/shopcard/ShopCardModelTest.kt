package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.ErrorState
import com.tokopedia.discovery2.datamapper.getComponent
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

        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
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
    fun `test for handleErrorState`() {
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

    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents returns false`() {
        viewModel.shopCardUseCase = shopCardUseCase
        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.getShopLoadState().value, true)
    }

    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents returns true and list is empty`() {
        val componentsItem: ComponentsItem = spyk()
        val viewModel: ShopCardViewModel =
            spyk(ShopCardViewModel(application, componentsItem, 99))
        viewModel.shopCardUseCase = shopCardUseCase

        mockkStatic(::getComponent)
        every { getComponent(any(), any()) } returns componentsItem

        val list = ArrayList<ComponentsItem>()
        list.add(componentsItem)
        every { viewModel.getShopList() } returns list
        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.fetchShopCardData()
        TestCase.assertEquals(viewModel.syncData.value, true)
        TestCase.assertEquals(componentsItem.verticalProductFailState, true)
        TestCase.assertEquals(componentsItem.errorState, ErrorState.EmptyComponentState)
        TestCase.assertEquals(componentsItem.shouldRefreshComponent, null)

        unmockkStatic(::getComponent)
    }

    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents returns false and list is not empty`() {
        viewModel.shopCardUseCase = shopCardUseCase
        val componentsItem = ComponentsItem(name = "xyz")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItem)
        every { viewModel.getShopList() } returns list
        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns false

        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.syncData.value, true)
    }

    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents returns true and list is not empty and next page not available`() {
        val componentsItem: ComponentsItem = spyk()
        val viewModel: ShopCardViewModel =
            spyk(ShopCardViewModel(application, componentsItem, 99))
        viewModel.shopCardUseCase = shopCardUseCase

        val componentsItemTemp = ComponentsItem(name = "xyz")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItemTemp)
        every { componentsItem.getComponentsItem() } returns list

        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true

        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.syncData.value, true)
        assert(viewModel.getShopCardItemsListData().value?.isNotEmpty() == true)
        assert(viewModel.getShopCardItemsListData().value?.size == 1)
    }

    @Test
    fun `test for fetchShopCardData when loadFirstPageComponents returns true and list is not empty and next page available`() {
        val componentsItem: ComponentsItem = spyk()
        val viewModel: ShopCardViewModel =
            spyk(ShopCardViewModel(application, componentsItem, 99))
        viewModel.shopCardUseCase = shopCardUseCase

        val componentsItemTemp = ComponentsItem(name = "xyz")
        val list = ArrayList<ComponentsItem>()
        list.add(componentsItemTemp)
        every { componentsItem.getComponentsItem() } returns list

        mockkStatic(::getComponent)
        every { getComponent(any(), any()) } returns componentsItem

        coEvery {
            shopCardUseCase.loadFirstPageComponents(componentsItem.id, componentsItem.pageEndPoint)
        } returns true
        mockkObject(Utils)
        every { Utils.nextPageAvailable(componentsItem, any()) } returns true

        viewModel.fetchShopCardData()

        TestCase.assertEquals(viewModel.syncData.value, true)
        assert(viewModel.getShopCardItemsListData().value?.isNotEmpty() == true)
        assert(viewModel.getShopCardItemsListData().value?.size == 2)
        assert(viewModel.getShopCardItemsListData().value?.last()?.name == ComponentNames.LoadMore.componentName)
        unmockkObject(Utils)
        unmockkStatic(::getComponent)
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
                componentsItem.id,
                componentsItem.pageEndPoint
            )
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
                componentsItem.id,
                componentsItem.pageEndPoint
            )
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
                componentsItem.id,
                componentsItem.pageEndPoint
            )
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
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns false

        viewModel.fetchShopCardPaginatedData()

        TestCase.assertEquals(viewModel.isLoadingData(), false)
    }

    /**************************** end of fetchShopCardPaginatedData() *******************************************/

    @Test
    fun `test for paginatedErrorData`() {
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem

        TestCase.assertEquals(viewModel.getShopList(), componentItem)
    }

    @Test
    fun `test for getShopCardList`() {
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem

        TestCase.assertEquals(viewModel.getShopList(), componentItem)
    }

    @Test
    fun `test for resetComponent`() {
        viewModel.resetComponent()

        TestCase.assertEquals(viewModel.components.noOfPagesLoaded, 0)
    }

    @Test
    fun `test for component passed to VM`() {
        TestCase.assertEquals(viewModel.components, componentsItem)
    }

    @Test
    fun `test for position passed`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for application passed`() {
        assert(viewModel.application == application)
    }

    @Test
    fun `test for getShopCardBackgroundData `() {
        val viewModel: ShopCardViewModel =
            spyk(ShopCardViewModel(application, componentsItem, 99))
        assert(viewModel.getShopCardBackgroundData().value == null)

        viewModel.onAttachToViewHolder()
        assert(viewModel.getShopCardBackgroundData().value == componentsItem)
    }
}
