package com.tokopedia.tokomart.search.presentation.viewmodel

import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateSearchViewModelTest: SearchTestFixtures() {

    override fun setUp() { }

    @Test
    fun `test create search view model`() {
        `When create view model`()

        `Then assert keyword from parameter`()
    }

    private fun `When create view model`() {
        setUpViewModel(defaultQueryParamMap)
    }

    private fun `Then assert keyword from parameter`() {
        assertThat(searchViewModel.query, shouldBe(defaultKeyword))
    }
}