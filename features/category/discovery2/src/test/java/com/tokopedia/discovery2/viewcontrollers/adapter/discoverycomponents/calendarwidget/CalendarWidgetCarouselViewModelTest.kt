package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.calendarwidget

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
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
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: CalendarWidgetCarouselViewModel = spyk(CalendarWidgetCarouselViewModel(application, componentsItem, 99))

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

        assertEquals(viewModel.getCalendarLoadState().value ,true)
    }

    @Test
    fun `test for fetchProductCarouselData`(){
        runBlocking {
            every { viewModel.getCalendarList() } returns mockk(relaxed = true)

            viewModel.fetchProductCarouselData()

            assertEquals(viewModel.getCalendarLoadState().value, true)
        }
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
    fun `test for resetComponent`(){
        viewModel.resetComponent()

        assertEquals(viewModel.components.noOfPagesLoaded, 0)
    }

    @Test
    fun `test for component passed to VM`(){
        assertEquals(viewModel.components, componentsItem)
    }

}