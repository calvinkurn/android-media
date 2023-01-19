package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import io.mockk.*
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
        mockkObject(Utils)
        mockkStatic(::getComponent)
        mockkConstructor(URLParser::class)
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

//    Doubt- are these methods testable? :
//    handleSaleEndSates
//    checkUpcomingSaleTimer
//    checkForTimerComponent
//    startTimer

    @Test
    fun `handleSaleEndSates when isFutureSale is true`() {
        every { Utils.isFutureSale(any()) } returns true

        viewModel.handleSaleEndSates()

        Assert.assertEquals(viewModel.syncData.value,null)
    }

    @Test
    fun `handleSaleEndSates when isFutureSaleOngoing is true`() {
        every { Utils.isFutureSale(any()) } returns true
        every { Utils.isFutureSaleOngoing(any(),any()) } returns true

        viewModel.handleSaleEndSates()

        Assert.assertEquals(viewModel.syncData.value,null)
    }

    @Test
    fun `handleSaleEndSates when isSaleOver is true`() {
        every { Utils.isFutureSale(any()) } returns false
        every { Utils.isFutureSaleOngoing(any(),any()) } returns false
        every { Utils.isSaleOver(any()) } returns true

        viewModel.handleSaleEndSates()

        Assert.assertEquals(viewModel.syncData.value,true)
    }

    @Test
    fun `checkUpcomingSaleTimer`() {
        val list = ArrayList<DataItem>()
        list.add(mockk(relaxed = true))
        coEvery { componentsItem.data } returns list
        coEvery { componentsItem.id } returns ""
        coEvery { getComponent(any(), any()) } returns componentsItem

        viewModel.checkUpcomingSaleTimer()

        Assert.assertEquals(viewModel.timerWithBannerCounter,null)
    }

    @Test
    fun `startTimer `() {
        val timerUnify: TimerUnifySingle = mockk(relaxed = true)
        val list = mutableListOf(DataItem(startDate = "2029-11-01 10:00:00"))
        coEvery { componentsItem.data } returns list
        every { Utils.isFutureSale(any()) } returns true

        viewModel.startTimer(timerUnify)

        Assert.assertEquals(viewModel.timerWithBannerCounter != null,false)
    }

    @Test
    fun `startTimer when isFutureSale is false`() {
        val timerUnify: TimerUnifySingle = mockk(relaxed = true)
        val list = mutableListOf(DataItem(startDate = "2029-11-01 10:00:00"))
        coEvery { componentsItem.data } returns list
        every { Utils.isFutureSale(any()) } returns false

        viewModel.startTimer(timerUnify)

        Assert.assertEquals(viewModel.timerWithBannerCounter != null,false)
    }

    @Test
    fun `startTimer when isFutureSale is true and saleTimeMillis less than zero`() {
        val timerUnify: TimerUnifySingle = mockk(relaxed = true)
        val list = mutableListOf(DataItem(startDate = "2020-11-01 10:00:00"))
        coEvery { componentsItem.data } returns list
        every { Utils.isFutureSale(any()) } returns true

        viewModel.startTimer(timerUnify)

        Assert.assertEquals(viewModel.timerWithBannerCounter != null,false)
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
        unmockkStatic(::getComponent)
        unmockkConstructor(URLParser::class)
    }
}
