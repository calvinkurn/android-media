package com.tokopedia.topads.view.adapter.product.viewmodel

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.product.ProductListAdapterTypeFactory
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KeyWordItemViewModelTest{

    private val keywordDataItem: KeywordDataItem = KeywordDataItem()
    private val factory: ProductListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: KeyWordItemViewModel

    @Before
    fun setUp() {
        viewModel = KeyWordItemViewModel(keywordDataItem)
    }
    @Test
    fun `type returns correct type from factory`(){
        val type = viewModel.type(factory)
        assertEquals(type, factory.type(viewModel))
    }

    @Test
    fun `set and get keywordDataItem returns correct value`(){
        val keywordDataItem = KeywordDataItem(String.EMPTY)
        viewModel.keywordDataItem = keywordDataItem
        assertEquals(keywordDataItem, viewModel.keywordDataItem)
    }

}
