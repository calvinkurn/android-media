package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.data.getInitialChecklistData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.ChecklistUiModel
import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductManageFilterExpandChecklistViewModelTest: ProductManageFilterExpandChecklistViewModelTestFixture() {

    @Test
    fun `when init data should update data and number of selected data accordingly`() {
        val dataToInsert = getInitialChecklistData()

        viewModel.initData(dataToInsert)

        val expectedCount = 2

        verifyChecklistDataEquals(dataToInsert)
        verifySelectCountEquals(expectedCount)
    }


    @Test
    fun `when clear all checklist should update select values to false`() {
        val initialData = getInitialChecklistData()

        viewModel.initData(initialData)
        viewModel.clearAllChecklist()

        verifyAllValuesAreFalse()
    }

    @Test
    fun `when update select should update actual data and number of selected data accordingly`() {
        val dataToInsert = getInitialChecklistData()
        val selectedData = dataToInsert.first()

        viewModel.initData(dataToInsert)
        viewModel.updateSelectedItem(selectedData)

        val expectedCount = 3
        val expectedData = ChecklistUiModel(id ="1", name = "Some Filter", value = "", isSelected = true)

        verifySelectCountEquals(expectedCount)
        verifyChecklistDataSelected(expectedData)
    }

    @Test
    fun `when update select selected data should update actual data and number of selected data accordingly`() {
        val dataToInsert = getInitialChecklistData()
        val selectedData = dataToInsert[2]

        viewModel.initData(dataToInsert)
        viewModel.updateSelectedItem(selectedData)

        val expectedCount = 1
        val expectedData = ChecklistUiModel(id ="3", name = "Some Other Filter", value = "", isSelected = false)

        verifySelectCountEquals(expectedCount)
        verifyChecklistDataSelected(expectedData)
    }

    @Test
    fun `when data is null all operations should do nothing`() {

        viewModel.updateSelectedItem(getInitialChecklistData().first())
        viewModel.clearAllChecklist()

        verifyDataIsNull()
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

    private fun verifyDataIsNull() {
        assertEquals(null, viewModel.checklistData.value)
    }


}