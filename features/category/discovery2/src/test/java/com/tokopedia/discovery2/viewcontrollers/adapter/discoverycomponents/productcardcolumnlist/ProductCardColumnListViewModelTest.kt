package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcolumnlist

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
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
import com.tokopedia.user.session.UserSessionInterface
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
    private val userSession: UserSessionInterface = mockk(relaxed = true)
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
    fun `test 2`() {
        every { componentsItem.getComponentItem(0) } returns null
        Assert.assertEquals(null, viewModel.getProduct(0))

        //
        val dataItem2 = ComponentsItem(
            data = listOf(),
            id = "12313"
        )
        every { componentsItem.getComponentItem(0) } returns dataItem2
        Assert.assertEquals(null, viewModel.getProduct(0))

        //
        val dataItem = ComponentsItem(
            data = null,
            id = "12313"
        )
        every { componentsItem.getComponentItem(0) } returns dataItem
        Assert.assertEquals(null, viewModel.getProduct(0))

        //
        val dataItem4 = ComponentsItem(
            data = listOf(
                DataItem(
                    id = "22222",
                    name = "Product Name 1"
                )
            ),
            id = "12313"
        )

        every { componentsItem.getComponentItem(0) } returns dataItem4
        Assert.assertEquals(dataItem4.data?.firstOrNull(), viewModel.getProduct(0))
    }

    @Test
    fun `test again`() {
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
            productCardsUseCase.loadFirstPageComponents(any(), any(), any())
        } returns true

        val components2: List<ComponentsItem> = listOf()

        every { componentsItem.getComponentsItem() } returns components2

        viewModel.onAttachToViewHolder()

        viewModel.errorState.getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test again 5`() {
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(), any(), any())
        } returns true

        every { componentsItem.getComponentsItem() } returns null

        viewModel.onAttachToViewHolder()

        viewModel.errorState.getOrAwaitValue()

        viewModel.errorState
            .verifyValueEquals(Unit)
    }

    @Test
    fun `test again 2`() {
        viewModel.productCardsUseCase = productCardsUseCase

        coEvery {
            productCardsUseCase.loadFirstPageComponents(any(), any(), any())
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

    @Test
    fun `login`() {
        Assert.assertEquals(false, viewModel.isLoggedIn())

        viewModel.userSession = userSession

        every {
            userSession.isLoggedIn
        } returns true

        Assert.assertEquals(true, viewModel.isLoggedIn())
    }
}
