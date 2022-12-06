package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ThematicHeaderViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    val viewModel: ThematicHeaderViewModel =
        spyk(ThematicHeaderViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `component value is present in live data`() {
        every { componentsItem.data } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.getComponentLiveData().value === componentsItem)
    }

    @Test
    fun `test for position passed`() {
        assert(viewModel.position == 99)
    }
}
