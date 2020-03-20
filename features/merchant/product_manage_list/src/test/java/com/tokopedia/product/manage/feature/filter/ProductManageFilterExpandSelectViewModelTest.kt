package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import junit.framework.Assert.*
import org.junit.Test

class ProductManageFilterExpandSelectViewModelTest: ProductManageFilterExpandSelectViewModelTestFixture() {

    @Test
    fun `when_update_data_should_set_select_data_to_desired_list`() {
        val dataToInsert = listOf(SelectUiModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
                SelectUiModel(id = "2", name = "Some Etalase", value = "", isSelected = false))

        viewModel.updateData(dataToInsert)

        verifyUpdatedDataEquals(dataToInsert)
    }

    @Test
    fun `when_update_select_index_less_than_5_should_return_false`() {
        val dataToInsert = listOf(SelectUiModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
                SelectUiModel(id = "2", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "3", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "4", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "5", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "6", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = false))
        val dataToSelect = dataToInsert[3]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertFalse(result)
    }

    @Test
    fun `when_update_select_index_greater_than_5_should_return_true`() {
        val dataToInsert = listOf(SelectUiModel(id = "1", name = "Some Sort", value = "DESC", isSelected = false),
                SelectUiModel(id = "2", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "3", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "4", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "5", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "6", name = "Some Etalase", value = "", isSelected = false),
                SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = false))
        val dataToSelect = dataToInsert[6]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertTrue(result)
    }

    private fun verifyUpdatedDataEquals(expectedData: List<SelectUiModel>) {
        val actualData = viewModel.selectData.value
        assertEquals(expectedData, actualData)
    }
}