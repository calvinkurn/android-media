package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp

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
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductCardRevampViewModelTest{
    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: ProductCardRevampViewModel by lazy {
        spyk(ProductCardRevampViewModel(application, componentsItem, 99))
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        coEvery { componentsItem.lihatSemua } returns null
    }

    @Test
    fun `position test`() {
        assert(viewModel.position == 99)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    @Test
    fun onAttachToViewHolder() {
        coEvery { componentsItem.lihatSemua } returns null
        coEvery { componentsItem.id } returns ""
        coEvery { componentsItem.pageEndPoint } returns ""
        val  viewModel = spyk(ProductCardRevampViewModel(application, componentsItem, 99))

        coEvery { viewModel.productCardsUseCase.loadFirstPageComponents(any(), any()) } returns true

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.syncData.value, true)
    }
    /**************************** onAttachToViewHolder() *******************************************/


    /**************************** onAttachToViewHolderError() *******************************************/

    @Test
    fun onAttachToViewHolderError() {
        runBlocking {
            coEvery { componentsItem.lihatSemua } returns null
            coEvery { componentsItem.id } returns ""
            coEvery { componentsItem.pageEndPoint } returns ""


            val  viewModel = spyk(ProductCardRevampViewModel(application, componentsItem, 99))

            val error = Throwable("something")
            coEvery { viewModel.productCardsUseCase.loadFirstPageComponents(any(), any()) } throws error

            viewModel.onAttachToViewHolder()

            assertEquals(viewModel.syncData.value, true)
        }
    }
    /**************************** onAttachToViewHolderError() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}