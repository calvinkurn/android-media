package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.viewallcard

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ViewAllCarouselViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val component = mockk<ComponentsItem>(relaxed = true)
    private val application = mockk<Application>()

    private lateinit var viewModel: ViewAllCarouselViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = spyk(ViewAllCarouselViewModel(application, component, 99))
    }

    @Test
    fun `given component's data is unavailable, should not emit any value`() {
        every { component.data } returns null

        viewModel.onAttachToViewHolder()

        assertNull(viewModel.getData().value)
    }

    @Test
    fun `given component's data is available, when view model is attached, should emit the component's data`() {
        val item = DataItem(
            title = "View All Card Title",
            applinks = "tokopedia://shop/2147991"
        )

        every { component.data } returns listOf(item)

        viewModel.onAttachToViewHolder()

        assert(item == viewModel.getData().value)
    }
}
