package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery.common.utils.URLParser
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.Properties
import com.tokopedia.discovery2.usecase.productCardCarouselUseCase.ProductCardsUseCase
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CalendarWidgetCarouselViewModelTest {
    @get:Rule
    val rule1 = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val context = mockk<Context>(relaxed = true)

    private val viewModel: CalendarWidgetCarouselViewModel = spyk(CalendarWidgetCarouselViewModel(application, componentsItem, 99))

    private val calenderWidgetUseCase: ProductCardsUseCase by lazy {
        mockk()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        mockkObject(Utils)
        mockkConstructor(URLParser::class)
        every { anyConstructed<URLParser>().paramKeyValueMapDecoded } returns HashMap()
        every { application.applicationContext } returns context
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        mockkObject(Utils)
        unmockkConstructor(URLParser::class)
    }

    @Test
    fun `test for handleErrorState`(){
        every { componentsItem.verticalProductFailState } returns true

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getCalendarLoadState().value ,true)
    }
    @Test
    fun `test for handleErrorState when verticalProductFailState is false`(){
        viewModel.calenderWidgetUseCase = calenderWidgetUseCase
        val list = ArrayList<ComponentsItem>()
        val dataList = arrayListOf<DataItem>()
        val dataItem = DataItem(hasNotifyMe = true)
        dataList.add(dataItem)
        val item = ComponentsItem(id = "2", pageEndPoint = "s", data = dataList)
        list.add(item)
        every { componentsItem.getComponentsItem() } returns list
        val tempProperties = Properties(calendarType = Constant.Calendar.DYNAMIC)
        every { componentsItem.properties } returns tempProperties
        coEvery { application.applicationContext } returns context
        coEvery { viewModel.calenderWidgetUseCase.loadFirstPageComponents(any(),any(),any()) } returns true
        val dataArray : ArrayList<DataItem> = mockk(relaxed = true)
        every {
            runBlocking {
                dataArray.getMaxHeightForCarouselView(context = context, coroutineDispatcher = rule1.dispatchers.default , Constant.Calendar.DYNAMIC )
            }
        } answers {
            1
        }

        viewModel.onAttachToViewHolder()

        assertEquals(viewModel.getCalendarLoadState().value ,false)
    }

    @Test
    fun `test for fetchProductCarouselData`() {
        every { componentsItem.properties } returns null
        every { viewModel.getCalendarList() } returns mockk(relaxed = true)

        viewModel.fetchProductCarouselData()

        assertEquals(viewModel.getCalendarLoadState().value, true)

        val properties = Properties()
        every { componentsItem.properties } returns properties
        every { viewModel.getCalendarList() } returns mockk(relaxed = true)

        viewModel.fetchProductCarouselData()

        assertEquals(viewModel.getCalendarLoadState().value, true)

        viewModel.calenderWidgetUseCase = calenderWidgetUseCase
        val properties1 = Properties(calendarType = Constant.Calendar.DYNAMIC)
        every { componentsItem.properties } returns properties1
        every { componentsItem.shouldRefreshComponent } returns false
        every { viewModel.getCalendarList() } returns mockk(relaxed = true)
        coEvery { viewModel.calenderWidgetUseCase.loadFirstPageComponents(any(),any(),any()) } returns true

        viewModel.fetchProductCarouselData()

        assertEquals(viewModel.getCalendarLoadState().value, true)

        viewModel.calenderWidgetUseCase = calenderWidgetUseCase
        val properties2 = Properties(calendarType = Constant.Calendar.DYNAMIC)
        every { componentsItem.properties } returns properties2
        every { componentsItem.shouldRefreshComponent } returns false
        every { viewModel.getCalendarList() } returns mockk(relaxed = true)
        coEvery { viewModel.calenderWidgetUseCase.loadFirstPageComponents(any(),any(),any()) } throws Exception("error")

        viewModel.fetchProductCarouselData()

        assertEquals(viewModel.getCalendarLoadState().value, true)

    }

    @Test
    fun `test for fetchCarouselPaginatedCalendars`(){
        runBlocking {
            coEvery {
                viewModel.calenderWidgetUseCase.getCarouselPaginatedData(
                    any(),
                    any(),
                    any()
                )
            } returns true
            every { viewModel.getCalendarList() } returns mockk(relaxed = true)

            viewModel.fetchCarouselPaginatedCalendars()

            assertEquals(viewModel.isLoadingData(), false)
        }
    }

    @Test
    fun `test for getCalendarList`(){
        val componentItem = mockk<ArrayList<ComponentsItem>>(relaxed = true)
        every { componentsItem.getComponentsItem() } returns componentItem

        assertEquals(viewModel.getCalendarList(), componentItem)
    }

    @Test
    fun `test for isLastPage`(){
        every { Utils.nextPageAvailable(any(),any()) } returns false

        assert(viewModel.isLastPage())
    }

    @Test
    fun `test for getPageSize`(){
        assert(viewModel.getPageSize() == 10)
    }

    @Test
    fun `test for resetComponent`(){
        viewModel.resetComponent()

        assertEquals(viewModel.components.noOfPagesLoaded, 0)
    }

    @Test
    fun `test for component passed to VM`(){
        assertEquals(viewModel.components, componentsItem)
    }

}
