package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TabsItemViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: TabsItemViewModel by lazy {
        spyk(TabsItemViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }


    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `component value is present in live data`() {
        viewModel.onAttachToViewHolder()

        assert(viewModel.getComponentLiveData().value == componentsItem)
    }

    @Test
    fun `test for application`(){
        assert(viewModel.application === application)
    }

    @Test
    fun `test for setSelectionTabItem`(){
        val list = ArrayList<DataItem>()
        val item = DataItem()
        item.isSelected = true
        list.add(item)
        coEvery { componentsItem.data } returns list

        viewModel.setSelectionTabItem(true)

        assert(viewModel.getSelectionChangeLiveData().value == true)


        viewModel.setSelectionTabItem(false)

        assert(viewModel.getSelectionChangeLiveData().value == false)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}