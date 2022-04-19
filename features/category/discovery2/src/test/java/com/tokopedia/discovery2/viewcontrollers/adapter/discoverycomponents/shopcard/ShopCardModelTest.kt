package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shopcard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.SectionUseCase
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

class ShopCardModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private var viewModel: ShopCardViewModel =
        spyk(ShopCardViewModel(application, componentsItem, 99))

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
    fun `test for handleErrorState`(){
        every { componentsItem.verticalProductFailState } returns true

        viewModel.onAttachToViewHolder()

        TestCase.assertEquals(viewModel.getShopLoadState().value, true)
    }

    @Test
    fun `test for fetchShopCardData`(){
        runBlocking {
            every { viewModel.getShopList() } returns mockk(relaxed = true)

            viewModel.fetchShopCardData()

            TestCase.assertEquals(viewModel.getShopLoadState().value, true)
        }
    }

    @Test
    fun `test for fetchShopCardPaginatedData`(){
        runBlocking {
            coEvery {
                viewModel.shopCardUseCase.getShopCardPaginatedData(
                        any(),
                        any(),
                        any()
                )
            } returns true
            every { viewModel.getShopList() } returns mockk(relaxed = true)

            viewModel.fetchShopCardPaginatedData()

            TestCase.assertEquals(viewModel.isLoadingData(), false)
        }
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