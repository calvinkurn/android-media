package com.tokopedia.filter.common.data

import com.tokopedia.filter.common.data.SavedOption.Companion.SORT_SAVED_OPTION_TITLE
import com.tokopedia.filter.testutils.jsonToObject
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class SavedOptionTest {

    @Test
    fun `create saved option will get key value and name from option`() {
        val option = Option(
            key = "dummy_key",
            value = "dummy_value",
            name = "dummy_name",
        )

        val savedOption = SavedOption.create(option, listOf())

        assertThat(savedOption.key, `is`(option.key))
        assertThat(savedOption.value, `is`(option.value))
        assertThat(savedOption.name, `is`(option.name))
    }

    @Test
    fun `create saved option will get title from filter that contains the option`() {
        val option = Option(
            key = "dummy_key",
            value = "dummy_value",
            name = "dummy_name",
        )
        val filterContainsOption = Filter(
            options = listOf(option),
            title = "dummy_title",
        )
        val filterDoesNotContainOption = Filter(
            title = "dummy_title_2"
        )

        val filterList = listOf(filterContainsOption, filterDoesNotContainOption)
        val savedOption = SavedOption.create(option, filterList)

        assertThat(savedOption.title, `is`(filterContainsOption.title))
    }

    @Test
    fun `create saved option from sort will have key value name from sort and hardcoded title`() {
        val dynamicFilterModel =
            "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val sort = dynamicFilterModel.data.sort[1]

        val savedOption = SavedOption.create(sort)

        assertThat(savedOption.key, `is`(sort.key))
        assertThat(savedOption.value, `is`(sort.value))
        assertThat(savedOption.name, `is`(sort.name))
        assertThat(savedOption.title, `is`(SORT_SAVED_OPTION_TITLE))
    }

    @Test
    fun `create saved option from level 2 category option will have title from category filter`() {
        val dynamicFilterModel =
            "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val filterList = dynamicFilterModel.data.filter
        val categoryFilter = filterList.find { it.isCategoryFilter }!!
        val categoryOptionLevel2 = findCategoryFilterLevel2(categoryFilter)

        val savedOption = SavedOption.create(categoryOptionLevel2, filterList)

        assertThat(savedOption.title, `is`(categoryFilter.title))
    }

    private fun findCategoryFilterLevel2(categoryFilter: Filter) =
        categoryFilter
            .options.first()
            .levelTwoCategoryList.first()
            .asOption()

    @Test
    fun `create saved option from level 3 category option will have title from category filter`() {
        val dynamicFilterModel =
            "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val filterList = dynamicFilterModel.data.filter
        val categoryFilter = filterList.find { it.isCategoryFilter }!!
        val categoryOptionLevel3 = findCategoryFilterLevel3(categoryFilter)

        val savedOption = SavedOption.create(categoryOptionLevel3, filterList)

        assertThat(savedOption.title, `is`(categoryFilter.title))
    }

    private fun findCategoryFilterLevel3(categoryFilter: Filter): Option {
        val categoryOptionLevel2 = categoryFilter
            .options.first()
            .levelTwoCategoryList.find { it.levelThreeCategoryList.isNotEmpty() }!!

        return categoryOptionLevel2
            .levelThreeCategoryList.find { it.value != categoryOptionLevel2.value }!!
            .asOption()
    }
}