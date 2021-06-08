package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import io.mockk.MockKAnnotations
import io.mockk.mockk
import io.mockk.spyk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DynamicCategoryItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: DynamicCategoryItemViewModel by lazy {
        spyk(DynamicCategoryItemViewModel(application, componentsItem, 99))
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun shutDown() {
    }
    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentLiveData().value == componentsItem)
    }
}