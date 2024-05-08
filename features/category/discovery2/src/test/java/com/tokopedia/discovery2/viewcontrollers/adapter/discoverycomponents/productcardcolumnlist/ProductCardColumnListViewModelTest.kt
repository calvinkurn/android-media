package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.usecase.topAdsUseCase.TopAdsTrackingUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist.ProductCardColumnListMapper.mapToCarouselPagingGroupProductModel
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductCardColumnListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val productCardsUseCase: ProductCardsUseCase = mockk(relaxed = true)
    private val topAdsUseCase: TopAdsTrackingUseCase = mockk(relaxed = true)

    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val componentsItem: ComponentsItem = mockk(relaxed = true)

    private val application: Application = mockk()
    private val position: Int = 99

    private var viewModel: ProductCardColumnListViewModel = spyk(
        ProductCardColumnListViewModel(
            application = application,
            components = componentsItem,
            position = position
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this)
        mockkConstructor(URLParser::class)
        mockUrlParser()
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
        unmockkConstructor(URLParser::class)
    }

    @Test
    fun `test for getting specific product`() {
        val componentId = "12313"
        val productId = "22222"
        val productName = "Product Name 1"

        // if item is null then expected result should be null as well
        val item1: ComponentsItem? = null
        mockComponentItem(item1)
        item1.verifyValueEquals(
            expected = viewModel.getProduct(
                position = position
            )
        )

        // if product (data of item) is empty then expected result should be null as well
        val item2 = ComponentsItem(
            data = listOf(),
            id = componentId
        )
        mockComponentItem(item2)
        item2.verifyValueEquals(
            expected = viewModel.getProduct(
                position = position
            )
        )

        // if product (data of item) is null then expected result should be null as well
        val item3 = ComponentsItem(
            data = null,
            id = componentId
        )
        mockComponentItem(item3)
        item3.verifyValueEquals(
            expected = viewModel.getProduct(
                position = position
            )
        )

        // if product (data of item) is there then expected result should the same
        val item4 = ComponentsItem(
            data = listOf(
                DataItem(
                    id = productId,
                    name = productName
                )
            ),
            id = componentId
        )

        mockComponentItem(item4)
        item4.verifyValueEquals(
            expected = viewModel.getProduct(
                position = position
            )
        )
    }

    @Test
    fun `test the success of getting carousel paging model`() {
        val componentId = "12313"
        val productId = "22222"
        val productName = "Product Name 1"

        val components: List<ComponentsItem> = mutableListOf(
            ComponentsItem(
                data = listOf(
                    DataItem(
                        id = productId,
                        name = productName
                    )
                ),
                id = componentId
            )
        )

        mockComponents(components)

        viewModel.onAttachToViewHolder()

        viewModel.carouselPagingGroupProductModel
            .getOrAwaitValue()

        viewModel.carouselPagingGroupProductModel
            .verifyValueEquals(
                expected = components.mapToCarouselPagingGroupProductModel()
            )
    }

    @Test
    fun `test the failed of getting carousel paging model when product list is empty`() {
        viewModel.productCardsUseCase = productCardsUseCase

        mockLoadFirstPageComponents(true)

        mockComponents(listOf())

        viewModel.onAttachToViewHolder()

        viewModel.errorState
            .getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test the failed of getting carousel paging model when product list is null`() {
        viewModel.productCardsUseCase = productCardsUseCase

        mockLoadFirstPageComponents(true)

        mockComponents(null)

        viewModel.onAttachToViewHolder()

        viewModel.errorState
            .getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test the failed of getting carousel paging model when there is a throwable  `() {
        viewModel.productCardsUseCase = productCardsUseCase

        mockLoadFirstPageComponents(Throwable())

        viewModel.onAttachToViewHolder()

        viewModel.errorState
            .getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test the value of view model's param`() {
        Assert.assertEquals(application, viewModel.application)

        Assert.assertEquals(componentsItem, viewModel.components)

        Assert.assertEquals(position, viewModel.position)
    }

    @Test
    fun `test user session data`() {
        // by default isLoggedIn() is false, make sure the data is the same as default value
        Assert.assertEquals(false, viewModel.isLoggedIn())

        viewModel.userSession = userSession

        val isLoggedIn = true

        mockLogin(isLoggedIn)

        // make sure isLoggedIn from view model is true
        Assert.assertEquals(isLoggedIn, viewModel.isLoggedIn())
    }

    @Test
    fun `test getting item per page when rows greater than product list size, the result should be the same as product list size`() {
        val propertyRows = 3
        val componentsItemSize = 2

        mockPropertyRows(propertyRows)

        mockComponentsItemSize(componentsItemSize)

        Assert.assertEquals(componentsItemSize, viewModel.getItemPerPage())
    }

    @Test
    fun `test getting item per page when rows lower than product list size, the result should be the same as rows`() {
        val propertyRows = 3
        val componentsItemSize = 4

        mockPropertyRows(propertyRows)

        mockComponentsItemSize(componentsItemSize)

        Assert.assertEquals(propertyRows, viewModel.getItemPerPage())
    }

    @Test
    fun `test getting item per page when rows the same as product list size, the result should be the same`() {
        val propertyRows = 4
        val componentsItemSize = 4

        mockPropertyRows(propertyRows)

        mockComponentsItemSize(componentsItemSize)

        Assert.assertEquals(propertyRows, viewModel.getItemPerPage())
    }

    @Test
    fun `test impression on top ads product, should track impression event`() {
        viewModel.topAdsTrackingUseCase = topAdsUseCase

        val componentId = "12313"
        val productId = "22222"
        val productName = "Product Name 1"
        val imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/$productId"
        val topAdsViewUrl =
            "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei"

        val topAdsItem = ComponentsItem(
            data = listOf(
                DataItem(
                    id = productId,
                    productId = productId,
                    name = productName,
                    imageUrlMobile = imageUrl,
                    topadsViewUrl = topAdsViewUrl,
                    isTopads = true
                )
            ),
            id = componentId
        )
        mockComponentItem(topAdsItem)

        mockTopAdsImpressionTrackingStatus(false)

        viewModel.trackTopAdsImpression(1)

        verify {
            topAdsUseCase
                .hitImpressions(
                    viewModel::class.qualifiedName,
                    topAdsViewUrl,
                    productId,
                    productName,
                    imageUrl
                )
        }
    }

    @Test
    fun `test click on top ads product, should track click event`() {
        viewModel.topAdsTrackingUseCase = topAdsUseCase

        val componentId = "12313"
        val productId = "22222"
        val productName = "Product Name 1"
        val imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/$productId"
        val topAdsClickUrl =
            "https://ta.tokopedia.com/promo/v1/clicks/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei"

        val topAdsItem = ComponentsItem(
            data = listOf(
                DataItem(
                    id = productId,
                    productId = productId,
                    name = productName,
                    imageUrlMobile = imageUrl,
                    topadsClickUrl = topAdsClickUrl,
                    isTopads = true
                )
            ),
            id = componentId
        )
        mockComponentItem(topAdsItem)

        viewModel.trackTopAdsClick(1)

        verify {
            topAdsUseCase
                .hitClick(
                    viewModel::class.qualifiedName,
                    topAdsClickUrl,
                    productId,
                    productName,
                    imageUrl
                )
        }
    }

    @Test
    fun `test impression on top ads product, should not track impression more than once`() {
        viewModel.topAdsTrackingUseCase = topAdsUseCase

        val componentId = "12313"
        val productId = "22222"
        val productName = "Product Name 1"
        val imageUrl = "https://images.tokopedia.net/img/cache/200-square/product-1/$productId"
        val topAdsViewUrl =
            "https://ta.tokopedia.com/promo/v1/views/8a-2rOYDQfPwgcV7yZUEHZFiy3zwq3ei"

        val topAdsItem = ComponentsItem(
            data = listOf(
                DataItem(
                    id = productId,
                    productId = productId,
                    name = productName,
                    imageUrlMobile = imageUrl,
                    topadsViewUrl = topAdsViewUrl,
                    isTopads = true
                )
            ),
            id = componentId
        )

        mockComponentItem(topAdsItem)

        mockTopAdsImpressionTrackingStatus(true)

        viewModel.trackTopAdsImpression(1)

        verify(exactly = 0) {
            topAdsUseCase
                .hitImpressions(
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
        }
    }

    //region private methods
    private fun mockComponents(components: List<ComponentsItem>?) {
        coEvery {
            componentsItem.getComponentsItem()
        } returns components
    }

    private fun mockLogin(isLoggedIn: Boolean) {
        every {
            userSession.isLoggedIn
        } returns isLoggedIn
    }

    private fun mockComponentItem(item: ComponentsItem?) {
        every {
            componentsItem.getComponentItem(any())
        } returns item
    }

    private fun mockPropertyRows(rows: Int) {
        every {
            componentsItem.getPropertyRows()
        } returns rows
    }

    private fun mockComponentsItemSize(size: Int) {
        every {
            componentsItem.getComponentsItemSize()
        } returns size
    }

    private fun mockUrlParser() {
        every {
            anyConstructed<URLParser>().paramKeyValueMapDecoded
        } returns hashMapOf()
    }

    private fun mockLoadFirstPageComponents(isSuccess: Boolean) {
        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(), any(), any())
        } returns isSuccess
    }

    private fun mockLoadFirstPageComponents(throwable: Throwable) {
        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(), any(), any())
        } throws throwable
    }

    private fun mockTopAdsImpressionTrackingStatus(isTracked: Boolean) {
        every {
            componentsItem.topAdsTrackingStatus
        } returns isTracked
    }

    private fun ComponentsItem?.verifyValueEquals(expected: Any?) {
        Assert.assertEquals(this?.data?.firstOrNull(), expected)
    }
    //endregion
}
