package com.tokopedia.topads.edit.view.adapter.keyword.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.edit.view.adapter.keyword.KeywordListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test


class KeywordItemViewModelTest {

    private val typesFactory: KeywordListAdapterTypeFactory = mockk(relaxed = true)
    private val keywordDataItem: KeywordDataItem = mockk(relaxed = true)
    private lateinit var viewModel: KeywordItemViewModel

    @Before
    fun setUp() {
        viewModel = KeywordItemViewModel(keywordDataItem)
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
