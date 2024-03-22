package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductShimmerViewModelTest {

    private val factory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: ProductShimmerViewModel

    @Before
    fun setUp() {
        viewModel = ProductShimmerViewModel()
    }

    @Test
    fun `type returns correct type from factory`() {
        val type = viewModel.type(factory)
        assertEquals(type, factory.type(viewModel))
    }

    @Test
    fun `set and get isChecked returns correct value`() {
        viewModel.isChecked = true
        assertEquals(true, viewModel.isChecked)
    }
}
