package com.tokopedia.topads.edit.view.adapter.product.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.edit.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ProductItemViewModelTest {

    private val typesFactory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private val productModel: TopAdsProductModel = mockk(relaxed = true)
    private lateinit var viewModel: ProductItemViewModel

    @Before
    fun setUp() {
        viewModel = ProductItemViewModel(productModel)
    }
    @Test
    fun `data should be set and retrieved correctly`() {
        val newData: TopAdsProductModel = mockk(relaxed = true)
        viewModel.data = newData
        assertEquals(newData, viewModel.data)
    }
    @Test
    fun `type should return type from typesFactory`() {
        val data = Int.ZERO
        every { typesFactory.type(viewModel) } returns Int.ZERO
        val actual = viewModel.type(typesFactory)
        assertEquals(data, actual)
    }

    @Test
    fun `isChecked should be set and retrieved correctly`() {
        viewModel.isChecked = true
        assertEquals(true, viewModel.isChecked)
    }
}
