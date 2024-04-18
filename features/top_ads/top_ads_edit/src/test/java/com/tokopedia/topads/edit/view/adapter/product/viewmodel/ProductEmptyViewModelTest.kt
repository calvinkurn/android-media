package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class ProductEmptyViewModelTest {

    private val typesFactory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: ProductEmptyViewModel

    @Before
    fun setUp() {
        viewModel = ProductEmptyViewModel()
    }

    @Test
    fun `type should return type from typesFactory`() {
        val data = Int.ZERO
        every { typesFactory.type(viewModel) } returns Int.ZERO
        val actual = viewModel.type(typesFactory)
        assertEquals(data, actual)
    }

}
