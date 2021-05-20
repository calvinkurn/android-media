package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateSearchViewModelTest: SearchTestFixtures() {

    override fun setUp() { }

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
        assertThat(searchViewModel.queryParam[SearchApiConst.OB], shouldBe(DEFAULT_VALUE_OF_PARAMETER_SORT))
    }
}