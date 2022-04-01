package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.*
import org.junit.*

class LihatSemuaViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val list = ArrayList<DataItem>()
    private val mockedDataItem:DataItem = mockk(relaxed = true)
    private val viewModel: LihatSemuaViewModel by lazy {
        spyk(LihatSemuaViewModel(application, componentsItem, 99)).apply {
            onAttachToViewHolder()
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentData().value == componentsItem)
    }

    @Test
    fun `test for position passed`(){
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for component passed to VM`(){
        assert(viewModel.component === componentsItem)
    }

    @Test
    fun `get Component Value`(){
        assert(viewModel.component === componentsItem)
    }

    @Test
    fun `start Date if no data present`() {
        every { componentsItem.data } returns null
        Assert.assertEquals(viewModel.getStartDate(), "")
        list.clear()
        every { componentsItem.data } returns list
        Assert.assertEquals(viewModel.getStartDate(), "")
        list.add(mockedDataItem)
        every { mockedDataItem.startDate } returns null
        Assert.assertEquals(viewModel.getStartDate(), "")
    }
    @Test
    fun `start Date if data is present`() {
        list.clear()
        list.add(mockedDataItem)
        every { componentsItem.data } returns list
        every { mockedDataItem.startDate  } returns "test date"
        Assert.assertEquals(viewModel.getStartDate(), "test date")
    }

    @Test
    fun `end Date if no data present`() {
        every { componentsItem.data } returns null
        Assert.assertEquals(viewModel.getEndDate(), "")
        list.clear()
        every { componentsItem.data } returns list
        Assert.assertEquals(viewModel.getEndDate(), "")
        list.add(mockedDataItem)
        every { mockedDataItem.endDate } returns null
        Assert.assertEquals(viewModel.getEndDate(), "")
    }

    @Test
    fun `end Date if data is present`() {
        list.clear()
        list.add(mockedDataItem)
        every { componentsItem.data } returns list
        every { mockedDataItem.endDate } returns "test date"
        Assert.assertEquals(viewModel.getEndDate(), "test date")
    }

    @Test
    fun `onResume called without onStop`(){
        viewModel.onResume()
        assert(viewModel.getRestartTimerAction().value == null)
    }

    @Test
    fun `onResume called after onStop`(){
        viewModel.onStop()
        viewModel.onResume()
        assert(viewModel.getRestartTimerAction().value == true)
    }

    @Test
    fun `onStop calls stopTimer`(){
        viewModel.onStop()
        verify { viewModel.stopTimer() }
    }

    @Test
    fun `onDetachViewHolder calls stopTimer`(){
        viewModel.onDetachToViewHolder()
        verify { viewModel.stopTimer() }
    }

    @Test
    fun `onDestroy calls stopTimer`(){
        viewModel.onDestroy()
        verify { viewModel.stopTimer() }
    }
}