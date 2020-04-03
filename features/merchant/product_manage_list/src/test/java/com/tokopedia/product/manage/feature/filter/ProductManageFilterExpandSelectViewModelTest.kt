package com.tokopedia.product.manage.feature.filter

import com.tokopedia.product.manage.data.getSelectedData
import com.tokopedia.product.manage.data.getUnselectedData
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.SelectUiModel
import junit.framework.Assert.*
import org.junit.Test

class ProductManageFilterExpandSelectViewModelTest: ProductManageFilterExpandSelectViewModelTestFixture() {

    @Test
    fun `when update data should set select data to desired list`() {
        val dataToInsert = getUnselectedData()

        viewModel.updateData(dataToInsert)

        verifyUpdatedDataEquals(dataToInsert)
    }

    @Test
    fun `when update select index less than 5 should return false`() {
        val dataToInsert = getUnselectedData()
        val dataToSelect = dataToInsert[3]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertFalse(result)
    }

    @Test
    fun `when update select index greater than 5 should return true`() {
        val dataToInsert = getUnselectedData()
        val dataToSelect = dataToInsert[6]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        assertTrue(result)
    }

    @Test
    fun `when data contains selected value update selected item should update selected and unselect old data`() {
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
    fun `when update selected item selected selected item should return false`() {
        val dataToInsert = getSelectedData()
        val dataToSelect = dataToInsert[dataToInsert.lastIndex]

        viewModel.updateData(dataToInsert)
        val result = viewModel.updateSelectedItem(dataToSelect)

        val expectedSelectedData = SelectUiModel(id = "7", name = "Some Etalase", value = "", isSelected = true)

        verifySelectUiModelEquals(expectedSelectedData)
        assertFalse(result)
    }

    @Test
    fun `when update selected item with no data should return false and data is still null`() {
        val dataToInsert = getSelectedData()
        val dataToSelect = dataToInsert[dataToInsert.lastIndex]

        val result = viewModel.updateSelectedItem(dataToSelect)

        assertFalse(result)
    }

    @Test
    fun `when data is null all operations should do nothing`() {

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