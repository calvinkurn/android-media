package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopoffersupportingbrand

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.supportingbrand.SupportingBrandLoadState
import com.tokopedia.discovery2.usecase.supportingbrand.SupportingBrandUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ShopOfferSupportingBrandViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val application = mockk<Application>()
    private val component = mockk<ComponentsItem>(relaxed = true)

    private val useCase: SupportingBrandUseCase by lazy { mockk(relaxed = true) }

    private lateinit var viewModel: ShopOfferSupportingBrandViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())

        mockkConstructor(URLParser::class)
        coEvery { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()

        viewModel = spyk(ShopOfferSupportingBrandViewModel(application, component, 99))

        viewModel.useCase = useCase
    }

    @Test
    fun `given loadFirstPageBrand is called, should get shop offer component data`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "tokopedia://discovery/discopagev2-shop-offer-supporting"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        viewModel.loadPageBrand()

        coVerify(exactly = 1) { useCase.loadPageComponents(componentId, pageEndpoint) }
    }

    @Test
    fun `given successfully get shop offer, should post the success result`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val componentsItems = arrayListOf<ComponentsItem>()
        componentsItems.add(ComponentsItem(name = ComponentNames.ShopOfferSupportingBrandItem.componentName))

        coEvery { component.getComponentsItem() } returns componentsItems

        coEvery {
            useCase.loadPageComponents(
                componentId,
                pageEndpoint
            )
        } returns SupportingBrandLoadState.LOAD_MORE

        viewModel.loadPageBrand()

        Assert.assertTrue(viewModel.brands.value is Success)
        Assert.assertEquals(componentsItems[0], (viewModel.brands.value as Success).data[0])
    }

    @Test
    fun `given failed to get shop offer, should post the fail result`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        coEvery { useCase.loadPageComponents(componentId, pageEndpoint) } throws Throwable()

        viewModel.loadPageBrand()

        Assert.assertTrue(viewModel.brands.value is Fail)
    }

    @Test
    fun `given failed to get shop offer due to null result, should post the fail result`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        coEvery {
            useCase.loadPageComponents(
                componentId,
                pageEndpoint
            )
        } returns SupportingBrandLoadState.FAILED

        viewModel.loadPageBrand()

        Assert.assertTrue(viewModel.brands.value is Fail)
        Assert.assertEquals("Empty Data", (viewModel.brands.value as Fail).throwable.message)
    }

    @Test
    fun `given successfully get shop offer but empty result, should post the fail result`() {
        val componentId = (0..Integer.MAX_VALUE).random().toString()
        val pageEndpoint = "discopagev2-flash-sale-toko-tab-test"

        coEvery { component.id } returns componentId

        coEvery { component.pageEndPoint } returns pageEndpoint

        val componentsItems = arrayListOf<ComponentsItem>()

        coEvery { component.getComponentsItem() } returns componentsItems

        coEvery {
            useCase.loadPageComponents(
                componentId,
                pageEndpoint
            )
        } returns SupportingBrandLoadState.LOAD_MORE

        viewModel.loadPageBrand()

        Assert.assertTrue(viewModel.brands.value is Fail)
        Assert.assertEquals("Empty Data", (viewModel.brands.value as Fail).throwable.message)
    }

    @Test
    fun `test for loadPageBrand with state LOAD_MORE`() {
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val list = ArrayList<ComponentsItem>()
        val result = Success(arrayListOf(componentsItem))
        for (i in 1..10)
            list.add(ComponentsItem(id = i.toString()))
        coEvery {
            useCase.loadPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns SupportingBrandLoadState.LOAD_MORE
        every { componentsItem.getComponentsItem() } returns list
        every { viewModel.brands.value } returns result
        viewModel.loadMore()

        assert(!viewModel.isLoading)
        assert(viewModel.brands.value == result)
    }

    @Test
    fun `test for loadPageBrand with state REACH_END_OF_PAGE`() {
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val list = ArrayList<ComponentsItem>()
        val result = Success(arrayListOf(componentsItem))
        for (i in 1..10)
            list.add(ComponentsItem(id = i.toString()))
        coEvery {
            useCase.loadPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns SupportingBrandLoadState.REACH_END_OF_PAGE
        every { componentsItem.getComponentsItem() } returns list
        every { viewModel.brands.value } returns result
        viewModel.loadMore()

        assert(!viewModel.isLoading)
        assert(viewModel.brands.value == result)
    }

    @Test
    fun `test for loadPageBrand with state FAILED`() {
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        val componentsItem: ComponentsItem = spyk()
        val list = ArrayList<ComponentsItem>()
        val result = Success(arrayListOf(componentsItem))
        for (i in 1..10)
            list.add(ComponentsItem(id = i.toString()))
        coEvery {
            useCase.loadPageComponents(
                componentsItem.id,
                componentsItem.pageEndPoint
            )
        } returns SupportingBrandLoadState.FAILED
        every { componentsItem.getComponentsItem() } returns list
        every { viewModel.brands.value } returns result
        viewModel.loadMore()

        assert(!viewModel.isLoading)
        assert(viewModel.brands.value == result)
    }
}
