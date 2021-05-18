package com.tokopedia.tokomart.category.presentation.viewmodel

import com.tokopedia.tokomart.searchcategory.OpenFilterPageTestHelper
import org.junit.Test

class CategoryOpenFilterPageTest: CategoryTestFixtures() {

    private val openFilterPageTestHelper: OpenFilterPageTestHelper = OpenFilterPageTestHelper(
            categoryViewModel,
            getFilterUseCase,
    )

    @Test
    fun `open filter page first time`() {
        openFilterPageTestHelper.`test open filter page first time`(mapOf())
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