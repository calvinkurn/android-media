package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistViewModel
import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductManageFilterExpandChecklistViewModelTest: ProductManageFilterExpandChecklistViewModelTestFixture() {

    @Test
    fun `when_init_data_should_update_daa_and_number_of_selected_data_accordingly`() {
        val dataToInsert = listOf(
                ChecklistViewModel(id ="1", name = "Some Filter", value = "", isSelected = true),
                ChecklistViewModel(id ="2", name = "Some Category", value = "", isSelected = false))

        viewModel.initData(dataToInsert)

        val expectedCount = 1

        verifyChecklistDataEquals(dataToInsert)
        verifySelectCountEquals(expectedCount)
    }


    @Test
    fun `when_clear_all_checklist_should_update_select_values_to_false`() {
        viewModel.clearAllChecklist()
        verifyAllValuesAreFalse()
    }

    private fun verifySelectCountEquals(expectedCount: Int) {
        val actualCount = viewModel.dataSize.value
        assertEquals(expectedCount, actualCount)
    }

    @Test
    fun `when_update_select_should_update_actual_data_and_number_of_selected_data_accordingly`() {
        val dataToInsert = listOf(
                ChecklistViewModel(id ="1", name = "Some Filter", value = "", isSelected = false),
                ChecklistViewModel(id ="2", name = "Some Category", value = "", isSelected = false))

        val selectedData = dataToInsert.first()

        viewModel.initData(dataToInsert)
        viewModel.updateSelectedItem(selectedData)

        val expectedCount = 1
        val expectedData = ChecklistViewModel(id ="1", name = "Some Filter", value = "", isSelected = true)

        verifySelectCountEquals(expectedCount)
        verifyChecklistDataSelected(expectedData)
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

    private fun verifyChecklistDataEquals(expectedData: List<ChecklistViewModel>) {
        val actualData = viewModel.checklistData.value?.toList()
        assertEquals(expectedData, actualData)
    }

    private fun verifyChecklistDataSelected(expectedData: ChecklistViewModel) {
        val actualData = viewModel.checklistData.value?.toList()
        actualData?.forEach {
            if(it.id == expectedData.id) {
                assertEquals(expectedData.isSelected, it.isSelected)
            }
        }
    }


}