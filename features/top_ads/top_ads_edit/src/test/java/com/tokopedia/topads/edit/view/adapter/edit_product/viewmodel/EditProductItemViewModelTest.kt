package com.tokopedia.topads.edit.view.adapter.edit_product.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.GetAdProductResponse
import com.tokopedia.topads.edit.view.adapter.edit_product.EditProductListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test


class EditProductItemViewModelTest {

    private val typesFactory: EditProductListAdapterTypeFactory = mockk(relaxed = true)
    private val productData: GetAdProductResponse.TopadsGetListProductsOfGroup.DataItem = mockk(relaxed = true)
    private lateinit var viewModel: EditProductItemViewModel

    @Before
    fun setUp() {
        viewModel = EditProductItemViewModel(productData)
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
