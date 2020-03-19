package com.tokopedia.product.manage.feature.filter

import junit.framework.Assert.assertEquals
import org.junit.Test

class ProductManageFilterExpandChecklistViewModelTest: ProductManageFilterExpandChecklistViewModelTestFixture() {

    @Test
    fun `when_clear_all_checklist_should_update_select_values_to_false`() {
        viewModel.clearAllChecklist()
        verifyAllValuesAreFalse()
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


}