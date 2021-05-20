package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateCategoryViewModelTest: CategoryTestFixtures() {

    override fun setUp() { }

    @Test
    fun `test create category view model`() {
        `Given category view model`()

        `Then assert query param has default sort`()
    }

    private fun `Then assert query param has default sort`() {
        assertThat(categoryViewModel.queryParam[SearchApiConst.OB], shouldBe(DEFAULT_VALUE_OF_PARAMETER_SORT))
    }
}