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
}