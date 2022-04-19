package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.MixLeft
import com.tokopedia.discovery2.data.Properties
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MixLeftEmptyViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()
    private val viewModel: MixLeftEmptyViewModel =
        spyk(MixLeftEmptyViewModel(application, componentsItem, 99))

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test for components`() {
        assert(viewModel.components === componentsItem)
    }

    @Test
    fun `test for position`() {
        assert(viewModel.position == 99)
    }

    @Test
    fun `test for mixLeftData`() {
        every { componentsItem.properties } returns null
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftBannerDataLD().value == null)

        val prop = mockk<Properties>()
        every { prop.mixLeft } returns null
        every { componentsItem.properties } returns prop
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftBannerDataLD().value == null)

        val mixLeft = mockk<MixLeft>()
        every { prop.mixLeft } returns mixLeft
        viewModel.onAttachToViewHolder()
        assert(viewModel.getMixLeftBannerDataLD().value === mixLeft)
    }
}