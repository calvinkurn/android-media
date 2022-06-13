package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShimmerViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: ShimmerViewModel by lazy {
        spyk(ShimmerViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    /**************************** test for design type *******************************************/
    @Test
    fun `design type test when properties is null`() {
        every { componentsItem.properties } returns null

        assert(viewModel.getTemplateType() == Constant.ProductTemplate.GRID)
    }
    @Test
    fun `design type test when template is null`() {
        every { componentsItem.properties?.template } returns null

        assert(viewModel.getTemplateType() == Constant.ProductTemplate.GRID)
    }

    @Test
    fun `design type test when template is not null`() {
        every { componentsItem.properties?.template } returns Constant.ProductTemplate.LIST

        assert(viewModel.getTemplateType() == Constant.ProductTemplate.LIST)
    }
    /**************************** end of design type *******************************************/

    /**************************** test for getCalendarLayout() *******************************************/
    @Test
    fun `test for getCalendarLayout when properties is null`() {
        every { componentsItem.properties } returns null

        assert(viewModel.getCalendarLayout() == null)
    }
    @Test
    fun `test for getCalendarLayout when calendarLayout is null`() {
        every { componentsItem.properties?.calendarLayout } returns null

        assert(viewModel.getCalendarLayout() == null)
    }

    @Test
    fun `test for getCalendarLayout when calendarLayout is not null`() {
        every { componentsItem.properties?.calendarLayout } returns "calendarLayout"

        assert(viewModel.getCalendarLayout() == "calendarLayout")
    }
    /**************************** end of getCalendarLayout() *******************************************/

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentData().value == componentsItem)
    }
    @Test
    fun `test for position passed to VM`(){
        assert(viewModel.position == 99)
    }
}