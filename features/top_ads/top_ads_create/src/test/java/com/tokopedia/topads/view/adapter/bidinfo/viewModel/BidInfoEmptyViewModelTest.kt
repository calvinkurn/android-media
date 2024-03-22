package com.tokopedia.topads.view.adapter.bidinfo.viewModel

import com.tokopedia.topads.view.adapter.bidinfo.BindInfoAdapterTypeFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BidInfoEmptyViewModelTest {

    private val factory: BindInfoAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: BidInfoEmptyViewModel

    @Before
    fun setUp() {
        viewModel = BidInfoEmptyViewModel()
    }

    @Test
    fun `type returns correct type from factory`() {
        val type = viewModel.type(factory)
        assertEquals(type, factory.type(viewModel))
    }
}
