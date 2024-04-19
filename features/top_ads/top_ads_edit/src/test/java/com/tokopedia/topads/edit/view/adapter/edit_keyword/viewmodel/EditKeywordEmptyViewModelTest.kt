package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class EditKeywordEmptyViewModelTest {

    private val typesFactory: EditKeywordListAdapterTypeFactory = mockk(relaxed = true)
    private lateinit var viewModel: EditKeywordEmptyViewModel

    @Before
    fun setUp() {
        viewModel = EditKeywordEmptyViewModel()
    }

    @Test
    fun `type should return type from typesFactory`() {
        val data = Int.ZERO
        every { typesFactory.type(viewModel) } returns Int.ZERO
        val actual = viewModel.type(typesFactory)
        assertEquals(data, actual)
    }
}
