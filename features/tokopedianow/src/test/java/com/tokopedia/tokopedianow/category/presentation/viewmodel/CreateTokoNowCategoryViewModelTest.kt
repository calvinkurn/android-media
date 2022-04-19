package com.tokopedia.tokopedianow.category.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.DEFAULT_VALUE_OF_PARAMETER_SORT
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.tokopedianow.searchcategory.CreateSearchCategoryViewModelTestHelper
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class CreateTokoNowCategoryViewModelTest:
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

    override fun getViewModel() = tokoNowCategoryViewModel

    @Test
    fun `test create category view model`() {
        `Given choose address data`()
        `Given category view model`()

        `Then assert query param has default sort`()
        `Then assert category ids`()
        `Then assert content is loading`()
        `Then assert category id for tracking`(defaultCategoryL1)
    }

    @Test
    fun `test create category view model with empty category`() {
        `Given choose address data`()
        `Given category view model`(categoryL1 = "", categoryL2 = "")

        `Then assert query param has default sort`()
        `Then assert category ids`(categoryL1 = "", categoryL2 = "")
        `Then assert content is loading`()
        `Then assert category id for tracking`(defaultCategoryL1 = "")
    }

    private fun `Then assert query param has default sort`() {
        assertThat(
                tokoNowCategoryViewModel.queryParam[SearchApiConst.OB],
                shouldBe(DEFAULT_VALUE_OF_PARAMETER_SORT)
        )
    }

    private fun `Then assert category ids`(
        categoryL1: String = defaultCategoryL1,
        categoryL2: String = defaultCategoryL2
    ) {
        assertThat(
                tokoNowCategoryViewModel.categoryL1,
                shouldBe(categoryL1)
        )
        assertThat(
                tokoNowCategoryViewModel.categoryL2,
                shouldBe(categoryL2)
        )
    }

    private fun `Then assert category id for tracking`(defaultCategoryL1: String) {
        assertThat(tokoNowCategoryViewModel.categoryIdTracking, shouldBe(defaultCategoryL1))
    }

    @Test
    fun `test create category view model with L2 category`() {
        val categoryL2 = "1333"

        `Given choose address data`()
        `Given category view model`(defaultCategoryL1, categoryL2, defaultQueryParamMap)

        `Then assert query param has exclude sc category L2`(categoryL2)
        `Then assert category id for tracking`("$defaultCategoryL1/$categoryL2")
    }

    private fun `Then assert query param has exclude sc category L2`(categoryL2: String) {
        assertThat(
                tokoNowCategoryViewModel.queryParam["${OptionHelper.EXCLUDE_PREFIX}${SearchApiConst.SC}"],
                shouldBe(categoryL2)
        )
    }

    private fun `Then assert content is loading`() {
        assertThat(tokoNowCategoryViewModel.isContentLoadingLiveData.value, shouldBe(true))
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