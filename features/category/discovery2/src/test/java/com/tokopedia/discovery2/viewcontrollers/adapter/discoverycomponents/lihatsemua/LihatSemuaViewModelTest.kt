package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

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

class LihatSemuaViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: LihatSemuaViewModel by lazy {
        spyk(LihatSemuaViewModel(application, componentsItem, 99)).apply {
            onAttachToViewHolder()
        }
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
        assert(viewModel.getComponentData().value == componentsItem)
    }
}