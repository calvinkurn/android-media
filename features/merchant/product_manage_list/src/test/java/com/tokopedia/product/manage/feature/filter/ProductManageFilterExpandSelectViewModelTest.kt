package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectViewModel
import junit.framework.Assert.*
import org.junit.Test

class ProductManageFilterExpandSelectViewModelTest: ProductManageFilterExpandSelectViewModelTestFixture() {

    @Test
    fun `when_update_data_should_set_select_data_to_desired_list`() {
        val dataToInsert = listOf(SelectViewModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
                SelectViewModel(id = "2", name = "Some Etalase", value = "", isSelected = false))

        viewModel.updateData(dataToInsert)

        verifyUpdatedDataEquals(dataToInsert)
    }

    @Test
    fun `when_update_select_index_less_than_5_should_return_false`() {
        val dataToInsert = listOf(SelectViewModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
                SelectViewModel(id = "2", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "3", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "4", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "5", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "6", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "7", name = "Some Etalase", value = "", isSelected = false))
        val dataToSelect = dataToInsert[3]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertFalse(result)
    }

    @Test
    fun `when_update_select_index_greater_than_5_should_return_true`() {
        val dataToInsert = listOf(SelectViewModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
                SelectViewModel(id = "2", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "3", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "4", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "5", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "6", name = "Some Etalase", value = "", isSelected = false),
                SelectViewModel(id = "7", name = "Some Etalase", value = "", isSelected = false))
        val dataToSelect = dataToInsert[6]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertTrue(result)
    }

    private fun verifyUpdatedDataEquals(expectedData: List<SelectViewModel>) {
        val actualData = viewModel.selectData.value
        assertEquals(expectedData, actualData)
    }
}