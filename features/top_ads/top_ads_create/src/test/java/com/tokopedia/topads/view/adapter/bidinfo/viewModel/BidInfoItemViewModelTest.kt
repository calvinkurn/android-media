package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BidInfoItemViewModelTest {

    private val data: KeywordDataItem = KeywordDataItem()
    private val factory: BindInfoAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: BidInfoItemViewModel

    @Before
    fun setUp() {
        viewModel = BidInfoItemViewModel(data)
    }

    @Test
    fun `type returns correct type from factory`() {
        val type = viewModel.type(factory)
        assertEquals(type, factory.type(viewModel))
    }

    @Test
    fun `set and get data returns correct value`() {
        val data = KeywordDataItem()
        viewModel.data = data
        assertEquals(data, viewModel.data)
    }
}
