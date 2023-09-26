package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist.ProductCardColumnListMapper.mapToCarouselPagingGroupProductModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.unit.test.ext.getOrAwaitValue
import com.tokopedia.unit.test.ext.verifyValueEquals
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductCardColumnListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val productCardsUseCase: ProductCardsUseCase = mockk(relaxed = true)
    private val componentItemPosition = 99
    private val application: Application = mockk()
    private var viewModel: ProductCardColumnListViewModel =
        spyk(ProductCardColumnListViewModel(application, componentsItem, componentItemPosition))
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
    fun `test 1`() {
        val components: List<ComponentsItem> = mutableListOf(
            ComponentsItem(
                data = listOf(
                    DataItem(
                        id = "22222",
                        name = "Product Name 1"
                    )
                ),
                id = "12313"
            ),
            ComponentsItem(
                data = listOf(
                    DataItem(
                        id = "33333",
                        name = "Product Name 2"
                    )
                ),
                id = "33122"
            ),
            ComponentsItem(
                data = listOf(
                    DataItem(
                        id = "44444",
                        name = "Product Name 3"
                    )
                ),
                id = "42133"
            )
        )
        every { componentsItem.getComponentsItem() } returns components

        components.forEachIndexed { index, item ->
            Assert.assertEquals(item.data?.firstOrNull(), viewModel.getProduct(index))
        }
    }

    @Test
    fun `test 2`() {
        every { componentsItem.getComponentsItem() } returns null
        Assert.assertEquals(viewModel.getProduct(0), null)

        val components1: List<ComponentsItem> = mutableListOf()
        every { componentsItem.getComponentsItem() } returns components1
        Assert.assertEquals(viewModel.getProduct(0), null)

        val components2: List<ComponentsItem> = mutableListOf(
            ComponentsItem(
                data = listOf(),
                id = "12313"
            )
        )
        every { componentsItem.getComponentsItem() } returns components2
        Assert.assertEquals(viewModel.getProduct(0), null)

        val components3: List<ComponentsItem> = mutableListOf(
            ComponentsItem(
                data = null,
                id = "12313"
            )
        )
        every { componentsItem.getComponentsItem() } returns components3
        Assert.assertEquals(viewModel.getProduct(0), null)
    }

    @Test
    fun `test for component type`() {
        // properties has shop card comp type
        val propertiesShopCardCompType = Properties(rows = "4")
        every { componentsItem.properties } returns propertiesShopCardCompType
        Assert.assertEquals(4, viewModel.getPropertyRows())

        // properties has empty comp type
        val propertiesEmptyCompType = Properties(rows = null)
        every { componentsItem.properties } returns propertiesEmptyCompType
        Assert.assertEquals(Int.ZERO, viewModel.getPropertyRows())

        // properties has null comp type
        val propertiesNullCompType = Properties(rows = String.EMPTY)
        every { componentsItem.properties } returns propertiesNullCompType
        Assert.assertEquals(Int.ZERO, viewModel.getPropertyRows())

        // properties is null
        every { componentsItem.properties } returns null
        Assert.assertEquals(Int.ZERO, viewModel.getPropertyRows())
    }

    @Test
    fun `test again`() {
        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(), any(), any())
        } returns true

        val components2: List<ComponentsItem> = mutableListOf(
            ComponentsItem(
                data = listOf(
                    DataItem(
                        id = "44444",
                        name = "Product Name 3"
                    )
                ),
                id = "42133"
            )
        )
        every { componentsItem.getComponentsItem() } returns components2

        viewModel.onAttachToViewHolder()

        viewModel.carouselPagingGroupProductModel.getOrAwaitValue()

        viewModel.carouselPagingGroupProductModel
            .verifyValueEquals(components2.mapToCarouselPagingGroupProductModel())
    }

    @Test
    fun `test again 1`() {
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(any(), any(), any())
        } returns true

        val components2: List<ComponentsItem> = listOf()

        every { componentsItem.getComponentsItem() } returns components2

        viewModel.onAttachToViewHolder()

        viewModel.errorState.getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test again 2`() {
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            viewModel.productCardsUseCase?.loadFirstPageComponents(any(), any(), any())
        } throws Throwable()

        viewModel.onAttachToViewHolder()

        viewModel.errorState.getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test again 3`() {
        Assert.assertEquals(application, viewModel.application)

        Assert.assertEquals(componentsItem, viewModel.components)

        Assert.assertEquals(componentItemPosition, viewModel.position)
    }
}
