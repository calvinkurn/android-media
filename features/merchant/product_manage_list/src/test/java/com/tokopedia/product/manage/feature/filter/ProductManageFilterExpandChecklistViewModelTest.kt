package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductManageFilterExpandChecklistViewModelTest: ProductManageFilterExpandChecklistViewModelTestFixture() {

    @Test
    fun `when_init_data_should_update_daa_and_number_of_selected_data_accordingly`() {
        val dataToInsert = getInitialData()

        viewModel.initData(dataToInsert)

        val expectedCount = 2

        verifyChecklistDataEquals(dataToInsert)
        verifySelectCountEquals(expectedCount)
    }


    @Test
    fun `when_clear_all_checklist_should_update_select_values_to_false`() {
        val initialData = getInitialData()

        viewModel.initData(initialData)
        viewModel.clearAllChecklist()

        verifyAllValuesAreFalse()
    }

    @Test
    fun `when_update_select_should_update_actual_data_and_number_of_selected_data_accordingly`() {
        val dataToInsert = getInitialData()
        val selectedData = dataToInsert.first()

        viewModel.initData(dataToInsert)
        viewModel.updateSelectedItem(selectedData)

        val expectedCount = 3
        val expectedData = ChecklistUiModel(id ="1", name = "Some Filter", value = "", isSelected = true)

        verifySelectCountEquals(expectedCount)
        verifyChecklistDataSelected(expectedData)
    }

    @Test
    fun `when_update_select_selected_data_should_update_actual_data_and_number_of_selected_data_accordingly`() {
        val dataToInsert = getInitialData()
        val selectedData = dataToInsert[2]

        viewModel.initData(dataToInsert)
        viewModel.updateSelectedItem(selectedData)

        val expectedCount = 1
        val expectedData = ChecklistUiModel(id ="3", name = "Some Other Filter", value = "", isSelected = false)

        verifySelectCountEquals(expectedCount)
        verifyChecklistDataSelected(expectedData)
    }

    private fun verifySelectCountEquals(expectedCount: Int) {
        val actualCount = viewModel.dataSize.value
        assertEquals(expectedCount, actualCount)
    }

    private fun verifyAllValuesAreFalse() {
        val actualChecklistData = viewModel.checklistData.value
        val actualNumberSelected = viewModel.dataSize.value
        val expectedNumberSelected = 0
        assertEquals(expectedNumberSelected, actualNumberSelected)
        actualChecklistData?.forEach {
            assertEquals(false,it.isSelected)
        }
    }

    private fun verifyChecklistDataEquals(expectedData: List<ChecklistUiModel>) {
        val actualData = viewModel.checklistData.value?.toList()
        assertEquals(expectedData, actualData)
    }

    private fun verifyChecklistDataSelected(expectedData: ChecklistUiModel) {
        val actualData = viewModel.checklistData.value?.toList()
        actualData?.forEach {
            if(it.id == expectedData.id) {
                assertEquals(expectedData.isSelected, it.isSelected)
            }
        }
    }

    private fun getInitialData(): List<ChecklistUiModel> {
        return listOf(
                ChecklistUiModel(id ="1", name = "Some Filter", value = "", isSelected = false),
                ChecklistUiModel(id ="2", name = "Some Category", value = "", isSelected = true),
                ChecklistUiModel(id ="3", name = "Some Other Filter", value = "", isSelected = true),
                ChecklistUiModel(id ="4", name = "Some Other Category", value = "", isSelected = false)
        )
    }


}