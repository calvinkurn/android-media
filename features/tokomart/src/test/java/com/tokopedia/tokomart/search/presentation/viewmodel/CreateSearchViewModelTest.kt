package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.tokomart.searchcategory.CreateSearchCategoryViewModelTestHelper
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateSearchViewModelTest:
        SearchTestFixtures(),
        CreateSearchCategoryViewModelTestHelper.Callback {

    private val testHelper = CreateSearchCategoryViewModelTestHelper(
            abTestPlatformWrapper,
            this,
    )

    override fun setUp() { }

    override fun `When create view model`() {
        `Given search view model`()
    }

    override fun getViewModel() = searchViewModel

    @Test
    fun `test create search view model`() {
        `Given search view model`()

        `Then assert keyword from parameter`()
        `Then assert query param has default sort`()
    }

    private fun `Then assert keyword from parameter`() {
        assertThat(searchViewModel.query, shouldBe(defaultKeyword))
    }

    private fun `Then assert query param has default sort`() {
        assertThat(
                searchViewModel.queryParam[SearchApiConst.OB],
                shouldBe(DEFAULT_VALUE_OF_PARAMETER_SORT)
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