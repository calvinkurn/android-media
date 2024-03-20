package com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.view.adapter.edit_neg_keyword.EditNegKeywordListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EditNegKeywordItemViewModelTest {

    private val typesFactory: EditNegKeywordListAdapterTypeFactory = mockk(relaxed = true)
    private val keywordItem: GetKeywordResponse.KeywordsItem = mockk(relaxed = true)
    private lateinit var viewModel: EditNegKeywordItemViewModel

    @Before
    fun setUp() {
        viewModel = EditNegKeywordItemViewModel(keywordItem)
    }

    @Test
    fun `type should return type from typesFactory`() {
        val data = Int.ZERO
        every { typesFactory.type(viewModel) } returns Int.ZERO
        val actual = viewModel.type(typesFactory)
        assertEquals(data, actual)
    }
}
