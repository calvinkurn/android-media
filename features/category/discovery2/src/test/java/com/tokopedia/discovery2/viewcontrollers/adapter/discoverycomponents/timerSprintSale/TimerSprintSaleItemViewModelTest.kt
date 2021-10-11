package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*

class TimerSprintSaleItemViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val mockedDataItem:DataItem = mockk(relaxed = true)
    private val list = ArrayList<DataItem>()

    private val viewModel: TimerSprintSaleItemViewModel by lazy {
        spyk(TimerSprintSaleItemViewModel(application, componentsItem, 0))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

//    Doubt- are these methods testable? :
//    handleSaleEndSates
//    checkUpcomingSaleTimer
//    checkForTimerComponent
//    startTimer

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
    fun `get timer variant `() {
        every { componentsItem.properties?.timerStyle  } returns null
        Assert.assertEquals(viewModel.getTimerVariant(), TimerUnifySingle.VARIANT_MAIN)
        every { componentsItem.properties?.timerStyle  } returns "main"
        Assert.assertEquals(viewModel.getTimerVariant(), TimerUnifySingle.VARIANT_MAIN)
        every { componentsItem.properties?.timerStyle  } returns "any"
        Assert.assertEquals(viewModel.getTimerVariant(), TimerUnifySingle.VARIANT_MAIN)
        every { componentsItem.properties?.timerStyle  } returns "informative"
        Assert.assertEquals(viewModel.getTimerVariant(), TimerUnifySingle.VARIANT_INFORMATIVE)
        every { componentsItem.properties?.timerStyle  } returns "inverted"
        Assert.assertEquals(viewModel.getTimerVariant(), TimerUnifySingle.VARIANT_ALTERNATE)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }
}