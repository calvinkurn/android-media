package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.data.getSelectedData
import com.tokopedia.product.manage.data.getUnselectedData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import junit.framework.Assert.*
import org.junit.Test

class ProductManageFilterExpandSelectViewModelTest: ProductManageFilterExpandSelectViewModelTestFixture() {

    @Test
    fun `when_update_data_should_set_select_data_to_desired_list`() {
        val dataToInsert = getUnselectedData()

        viewModel.updateData(dataToInsert)

        verifyUpdatedDataEquals(dataToInsert)
    }

    @Test
    fun `when_update_select_index_less_than_5_should_return_false`() {
        val dataToInsert = getUnselectedData()
        val dataToSelect = dataToInsert[3]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertFalse(result)
    }

    @Test
    fun `when_update_select_index_greater_than_5_should_return_true`() {
        val dataToInsert = getUnselectedData()
        val dataToSelect = dataToInsert[6]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertTrue(result)
    }

    @Test
    fun `when_data_contains_selected_value_update_selected_item_should_update_selected_and unselect_old_data`() {
        val dataToInsert = getSelectedData()
        val dataToSelect = dataToInsert.first()

        viewModel.updateData(dataToInsert)
        viewModel.updateSelectedItem(dataToSelect)

        val expectedSelectData = SelectUiModel(id = "1", name = "Some Sort", value = "DESC", isSelected = true)
        val expectedUnselectedData = SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = false)

        verifySelectUiModelEquals(expectedSelectData)
        verifySelectUiModelEquals(expectedUnselectedData)
    }

    @Test
    fun `when_update_selected_item_selected_selected_item_should_return_false`() {
        val dataToInsert = getSelectedData()
        val dataToSelect = dataToInsert[dataToInsert.lastIndex]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        val expectedSelectedData = SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = true)

        verifySelectUiModelEquals(expectedSelectedData)
        assertFalse(result)
    }

    @Test
    fun `when_update_selected_item_with_no_data_should_return_false_and_data_is_still_null`() {
        val dataToInsert = getSelectedData()
        val dataToSelect = dataToInsert[dataToInsert.lastIndex]

        val result = viewModel.updateSelectedItem(dataToSelect)

        assertFalse(result)
    }

    @Test
    fun `when_data_is_null_all_operations_should_do_nothing`() {

        viewModel.updateSelectedItem(getUnselectedData().first())

        verifyDataIsNull()
    }

    private fun verifyUpdatedDataEquals(expectedData: List<SelectUiModel>) {
        val actualData = viewModel.selectData.value
        assertEquals(expectedData, actualData)
    }

    private fun verifySelectUiModelEquals(expectedModel: SelectUiModel) {
        val actualData = viewModel.selectData.value
        actualData?.forEach {
            if(it.id == expectedModel.id) {
                assertEquals(expectedModel.name, it.name)
                assertEquals(expectedModel.value, it.value)
                assertEquals(expectedModel.isSelected, it.isSelected)
            }
        }
    }

    private fun verifyDataIsNull() {
        assertEquals(null, viewModel.selectData.value)
    }
}