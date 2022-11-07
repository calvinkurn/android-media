package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.usecase.ChipSelectionUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
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

class ChipsFilterViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val componentsItem: ComponentsItem = mockk()
    private val application: Application = mockk()
    private val viewModel: ChipsFilterViewModel by lazy {
        spyk(ChipsFilterViewModel(application, componentsItem, 99))
    }
    private val useCase:ChipSelectionUseCase = mockk()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { componentsItem.data } returns null
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
    fun onAttachToViewHolderEmpty() {
        val list = mockk<ArrayList<ComponentsItem>>()
        every { componentsItem.getComponentsItem() } returns list

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getListDataLiveData().value, list)
    }

    /**************************** onAttachToViewHolder() *******************************************/

    /**************************** onChipSelected() *******************************************/

    @Test
    fun onChipSelected() {
        runBlocking {
            val id = "5"
            val list = mockk<ArrayList<ComponentsItem>>(relaxed = true)
            every { componentsItem.getComponentsItem() } returns list
            every { componentsItem.id } returns "s"
            every { componentsItem.pageEndPoint } returns "s"
            viewModel.chipSelectionUseCase = useCase
            every { useCase.onChipSelection(any(),any(),any()) } returns true

            viewModel.onChipSelected(id)

            assertEquals(viewModel.syncData.value, true)
        }
    }

    /**************************** onChipSelected() *******************************************/

    /**************************** onChipUnSelected() *******************************************/

    @Test
    fun onChipUnSelected() {
        runBlocking {
            val id = "5"
            val list = mockk<ArrayList<ComponentsItem>>(relaxed = true)
            every { componentsItem.getComponentsItem() } returns list
            every { componentsItem.id } returns "s"
            every { componentsItem.pageEndPoint } returns "s"
            viewModel.chipSelectionUseCase = useCase
            every { useCase.onChipUnSelection(any(),any()) } returns true

            viewModel.onChipUnSelected(id)

            assertEquals(viewModel.syncData.value, true)
        }
    }

    /**************************** onChipUnSelected() *******************************************/


    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}
