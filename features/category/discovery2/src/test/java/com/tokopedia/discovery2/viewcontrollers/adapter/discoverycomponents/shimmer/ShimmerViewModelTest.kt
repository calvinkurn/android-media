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
        spyk(ShimmerViewModel(application, componentsItem, 0))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `design type test`() {
        every { componentsItem.properties } returns null
        assert(viewModel.getTemplateType() == Constant.ProductTemplate.GRID)
        every { componentsItem.properties?.template } returns Constant.ProductTemplate.GRID
        assert(viewModel.getTemplateType() == Constant.ProductTemplate.GRID)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentData().value == componentsItem)
    }
}