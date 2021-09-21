package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel.textcomponent

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer.ShimmerViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent.TextComponentViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TextComponentViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `component value is present in live data`() {
        every { componentsItem.data } returns null
        var viewModel: TextComponentViewModel = spyk(TextComponentViewModel(application, componentsItem, 0))
        assert(viewModel.getTextComponentLiveData().value == null)
        val item = DataItem()
        val list = ArrayList<DataItem>()
        every { componentsItem.data } returns list
        viewModel= spyk(TextComponentViewModel(application, componentsItem, 0))
        assert(viewModel.getTextComponentLiveData().value == null)
        list.add(item)
        viewModel= spyk(TextComponentViewModel(application, componentsItem, 0))
        assert(viewModel.getTextComponentLiveData().value === item)

    }
}