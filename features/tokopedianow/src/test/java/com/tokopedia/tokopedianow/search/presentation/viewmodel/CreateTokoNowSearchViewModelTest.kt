package com.tokopedia.tokopedianow.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.tokopedianow.searchcategory.CreateSearchCategoryViewModelTestHelper
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateTokoNowSearchViewModelTest:
        SearchTestFixtures(),
        CreateSearchCategoryViewModelTestHelper.Callback {

    private val testHelper = CreateSearchCategoryViewModelTestHelper(
            this,
    )

    override fun setUp() { }

    override fun `When create view model`() {
        `Given search view model`()
    }

    override fun getViewModel() = tokoNowSearchViewModel

    @Test
    fun `test create search view model`() {
        `Given choose address data`()
        `Given search view model`()

        `Then assert keyword from parameter`()
        `Then assert query param has default sort`()
        `Then assert content is loading`()
    }

    private fun `Then assert keyword from parameter`() {
        assertThat(tokoNowSearchViewModel.query, shouldBe(defaultKeyword))
    }

    private fun `Then assert query param has default sort`() {
        assertThat(
                tokoNowSearchViewModel.queryParam[SearchApiConst.OB],
                shouldBe(DEFAULT_VALUE_OF_PARAMETER_SORT)
        )
    }

    private fun `Then assert content is loading`() {
        assertThat(tokoNowSearchViewModel.isContentLoadingLiveData.value, shouldBe(true))
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
