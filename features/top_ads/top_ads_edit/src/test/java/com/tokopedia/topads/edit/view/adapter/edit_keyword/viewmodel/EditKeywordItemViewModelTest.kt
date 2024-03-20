package com.tokopedia.topads.edit.view.adapter.edit_keyword.viewmodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.common.data.response.KeySharedModel
import com.tokopedia.topads.edit.view.adapter.edit_keyword.EditKeywordListAdapterTypeFactory
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class EditKeywordItemViewModelTest {

    private val typesFactory: EditKeywordListAdapterTypeFactory = mockk(relaxed = true)
    private val keySharedModel: KeySharedModel = mockk(relaxed = true)
    private lateinit var viewModel: EditKeywordItemViewModel


    @Before
    fun setUp() {
        viewModel = EditKeywordItemViewModel(keySharedModel)
    }

    @Test
    fun `data should be set and retrieved correctly`() {
        val newData: KeySharedModel = mockk(relaxed = true)
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
}
