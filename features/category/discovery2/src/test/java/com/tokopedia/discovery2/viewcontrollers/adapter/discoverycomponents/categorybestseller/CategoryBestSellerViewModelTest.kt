package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.datamapper.getComponent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class CategoryBestSellerViewModelTest{

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: CategoryBestSellerViewModel by lazy {
        spyk(CategoryBestSellerViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())

        mockkStatic(::getComponent)
        coEvery { getComponent(any(), any()) } returns componentsItem
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.components == componentsItem)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun onAttachToViewHolderError() {
        runBlocking {
            val error = Error("error")
            every { componentsItem.id } returns "s"
            every { componentsItem.pageEndPoint } returns "s"
            every { componentsItem.properties } returns null
            coEvery {
                viewModel.productCardsUseCase.loadFirstPageComponents(
                    any(),
                    any(),
                    any()
                )
            } throws error

            viewModel.onAttachToViewHolder()

            Assert.assertEquals(viewModel.getProductLoadState().value, true)
        }
    }

    @Test
    fun onAttachToViewHolderEmpty() {
        runBlocking {
            val list = mockk<ArrayList<ComponentsItem>>()
            every { componentsItem.id } returns "s"
            every { componentsItem.pageEndPoint } returns "s"
            every { componentsItem.getComponentsItem() } returns list
            every { componentsItem.properties } returns null
            coEvery {
                viewModel.productCardsUseCase.loadFirstPageComponents(
                    any(),
                    any(),
                    any()
                )
            } returns mockk()

            viewModel.onAttachToViewHolder()

            Assert.assertEquals(viewModel.getProductLoadState().value, true)
        }
    }

    @Test
    fun onUpdateComponent(){
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        every { componentsItem.properties } returns null
        coEvery {
            viewModel.productCardsUseCase.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getBackgroundImage().value == null)
    }

    @Test
    fun `backgroundImage not present`(){
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        val mockProperties = mockk<Properties>()
        every { componentsItem.properties } returns mockProperties
        every { mockProperties.backgroundImageUrl } returns "url"
        coEvery {
            viewModel.productCardsUseCase.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getBackgroundImage().value == "url")

    }

    @Test
    fun `backgroundImage is present`(){
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.id } returns "s"
        every { componentsItem.pageEndPoint } returns "s"
        every { componentsItem.getComponentsItem() } returns list
        every { componentsItem.properties } returns null
        coEvery {
            viewModel.productCardsUseCase.loadFirstPageComponents(
                any(),
                any(),
                any()
            )
        } returns mockk()

        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentData().value === componentsItem)
    }

    /**************************** onAttachToViewHolder() *******************************************/


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()

        unmockkStatic(::getComponent)
    }
}
