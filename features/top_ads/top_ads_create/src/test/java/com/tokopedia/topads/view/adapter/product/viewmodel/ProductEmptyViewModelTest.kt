package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductEmptyViewModelTest {

    private val factory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: ProductEmptyViewModel

    @Before
    fun setUp() {
        viewModel = ProductEmptyViewModel()
    }

    @Test
    fun `type returns correct type from factory`() {
        val type = viewModel.type(factory)
        assertEquals(type, factory.type(viewModel))
    }
}
