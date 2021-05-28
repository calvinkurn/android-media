package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.tokomart.searchcategory.CreateSearchCategoryViewModelTestHelper
import com.tokopedia.tokomart.searchcategory.presentation.viewmodel.BaseSearchCategoryViewModel
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateCategoryViewModelTest:
        CategoryTestFixtures(),
        CreateSearchCategoryViewModelTestHelper.Callback {

    private val testHelper = CreateSearchCategoryViewModelTestHelper(
            abTestPlatformWrapper,
            this,
    )

    override fun setUp() { }

    override fun `When create view model`() {
        `Given category view model`()
    }

    override fun getViewModel() = categoryViewModel

    @Test
    fun `test create category view model`() {
        `Given category view model`()

        `Then assert query param has default sort`()
        `Then assert category id`()
    }

    private fun `Then assert query param has default sort`() {
        assertThat(
                categoryViewModel.queryParam[SearchApiConst.OB],
                shouldBe(DEFAULT_VALUE_OF_PARAMETER_SORT)
        )
    }

    private fun `Then assert category id`() {
        assertThat(
                categoryViewModel.categoryId,
                shouldBe(defaultCategoryId)
        )
    }

    @Test
    fun `test has global menu for variant navigation revamp`() {
        testHelper.`test has global menu for variant navigation revamp`()
    }

    @Test
    fun `test has global menu for variant old navigation`() {
        testHelper.`test has global menu for variant old navigation`()
    }
}