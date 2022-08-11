package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorybestseller

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import kotlin.collections.ArrayList

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

    /**************************** onAttachToViewHolder() *******************************************/


    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()

        unmockkStatic(::getComponent)
    }
}