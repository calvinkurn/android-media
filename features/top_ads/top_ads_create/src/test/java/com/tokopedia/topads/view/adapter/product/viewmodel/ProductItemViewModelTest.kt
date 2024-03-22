package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.topads.common.data.response.TopAdsProductModel
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ProductItemViewModelTest {

    private val data: TopAdsProductModel = TopAdsProductModel()
    private val factory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: ProductItemViewModel

    @Before
    fun setUp() {
        viewModel = ProductItemViewModel(data)
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

    @Test
    fun `set and get isCompact returns correct value`() {
        viewModel.isCompact = true
        assertEquals(true, viewModel.isCompact)
    }

    @Test
    fun `set and get data returns correct value`() {
        val data = TopAdsProductModel()
        viewModel.data = data
        assertEquals(data, viewModel.data)
    }
}
