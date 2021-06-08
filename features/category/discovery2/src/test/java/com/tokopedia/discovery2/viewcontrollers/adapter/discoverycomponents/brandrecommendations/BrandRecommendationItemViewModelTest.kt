package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BrandRecommendationItemViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    private val componentsItem: ComponentsItem = mockk(relaxed = true)
    private val application: Application = mockk()

    private val viewModel: BrandRecommendationItemViewModel by lazy {
        spyk(BrandRecommendationItemViewModel(application, componentsItem, 0)).apply {
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
    fun `design type test`() {
        every { componentsItem.design } returns "v1"
        assert(viewModel.getDesignType() == "v1")
    }

    @Test
    fun `component item test`() {
        every { componentsItem.data } returns null
        assert(viewModel.getComponentItem() == null)
        val list = ArrayList<DataItem>()
        val dataItem = DataItem()
        list.add(dataItem)
        every { componentsItem.data } returns list
        assert(viewModel.getComponentItem() == dataItem)
    }

    @Test
    fun `component value is present in live data`() {
        assert(viewModel.getComponentDataLiveData().value == componentsItem)
    }


}