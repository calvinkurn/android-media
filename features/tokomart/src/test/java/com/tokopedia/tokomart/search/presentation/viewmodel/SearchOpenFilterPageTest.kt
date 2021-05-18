package com.tokopedia.tokomart.search.presentation.viewmodel

import com.tokopedia.tokomart.searchcategory.OpenFilterPageTestHelper
import org.junit.Test

class SearchOpenFilterPageTest: SearchTestFixtures() {

    private lateinit var openFilterPageTestHelper: OpenFilterPageTestHelper

    override fun setUp() {
        super.setUp()

        openFilterPageTestHelper = OpenFilterPageTestHelper(
                searchViewModel,
                getFilterUseCase,
        )
    }

    @Test
    fun `open filter page first time`() {
        openFilterPageTestHelper.`test open filter page first time`(defaultQueryParamMap)
    }

    @Test
    fun `open filter page cannot be spammed`() {
        openFilterPageTestHelper.`test open filter page cannot be spammed`()
    }

    @Test
    fun `dismiss filter page`() {
        openFilterPageTestHelper.`test dismiss filter page`()
    }

    @Test
    fun `open filter page second time should not call API again`() {
        openFilterPageTestHelper.`test open filter page second time should not call API again`()
    }
}