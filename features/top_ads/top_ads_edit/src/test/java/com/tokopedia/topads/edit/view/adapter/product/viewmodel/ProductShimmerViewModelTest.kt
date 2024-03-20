package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ProductShimmerViewModelTest {

    private val typesFactory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: ProductShimmerViewModel

    @Before
    fun setUp() {
        viewModel = ProductShimmerViewModel()
    }

    @Test
    fun `type should return type from typesFactory`() {
        val data = Int.ZERO
        every { typesFactory.type(viewModel) } returns Int.ZERO
        val actual = viewModel.type(typesFactory)
        assertEquals(data, actual)
    }

    @Test
    fun `isChecked should be initially false`() {
        assertFalse(viewModel.isChecked)
    }
}
