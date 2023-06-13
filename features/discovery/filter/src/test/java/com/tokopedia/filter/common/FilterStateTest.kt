package com.tokopedia.filter.common

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.getCategoryFilter
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper.optionList
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.OPTION_SEPARATOR
import com.tokopedia.filter.testutils.jsonToObject
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FilterStateTest {

    private val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()

    @Test
    fun `filterViewState is set of Option unique id from parameter`() {
        val filterList = dynamicFilterModel.data.filter
        val nonFilterPair = SearchApiConst.Q to "samsung"
        val filterOption = filterList.optionList().find { it.key == SearchApiConst.OFFICIAL }!!
        val filterPair = filterOption.key to filterOption.value
        val parameter = mapOf(nonFilterPair, filterPair)

        val filterState = FilterState().from(
            parameter,
            filterList,
        )

        assertThat(filterState.filterViewState, `is`(setOf(filterOption.uniqueId)))
        assertTrue(filterState.isFilterActive)
    }

    @Test
    fun `filterViewState is will take value from parameter for input type text`() {
        val filterList = dynamicFilterModel.data.filter
        val filterOption = filterList.optionList().find { it.key == SearchApiConst.PMIN }!!
        val pminFilterValue = 10_000.toString()
        val parameter = mapOf(
            SearchApiConst.Q to "samsung",
            filterOption.key to pminFilterValue,
        )

        val filterState = FilterState().from(
            parameter,
            filterList,
        )

        val expectedUniqueId = filterOption.clone().apply { value = pminFilterValue }.uniqueId
        assertThat(filterState.filterViewState, `is`(setOf(expectedUniqueId)))
    }

    @Test
    fun `filterViewState for multiple options with same key`() {
        val filterList = dynamicFilterModel.data.filter
        val filterOption = filterList.optionList().filter { it.key == SearchApiConst.FCITY }
        val locationFilterOptionOne = filterOption.first()
        val locationFilterOptionTwo = filterOption[1]
        val parameter = mapOf(
            SearchApiConst.Q to "samsung",
            SearchApiConst.FCITY to
                locationFilterOptionOne.value + OPTION_SEPARATOR + locationFilterOptionTwo.value,
        )

        val filterState = FilterState().from(
            parameter,
            filterList,
        )

        assertThat(
            filterState.filterViewState,
            `is`(setOf(
                locationFilterOptionOne.uniqueId,
                locationFilterOptionTwo.uniqueId
            ))
        )
    }

    @Test
    fun `filterViewState for category filter level 2`() {
        val categoryLevelOption = dynamicFilterModel.getCategoryFilter().options.first()
        val levelTwoCategory = categoryLevelOption.levelTwoCategoryList[1]

        val parameter = mapOf(
            SearchApiConst.Q to "samsung",
            levelTwoCategory.key to levelTwoCategory.value,
        )

        val filterState = FilterState().from(
            parameter,
            dynamicFilterModel.data.filter,
        )

        assertThat(
            filterState.filterViewState,
            `is`(setOf(levelTwoCategory.asOption().uniqueId))
        )
    }

    @Test
    fun `filterViewState for category filter level 3`() {
        val categoryLevelOption = dynamicFilterModel.getCategoryFilter().options.first()
        val levelTwoCategory = categoryLevelOption.levelTwoCategoryList[1]
        val levelThreeCategory = levelTwoCategory.levelThreeCategoryList[1]

        val parameter = mapOf(
            SearchApiConst.Q to "samsung",
            levelThreeCategory.key to levelThreeCategory.value,
        )

        val filterState = FilterState().from(
            parameter,
            dynamicFilterModel.data.filter,
        )

        assertThat(
            filterState.filterViewState,
            `is`(setOf(levelThreeCategory.asOption().uniqueId))
        )
    }

    @Test
    fun `activeFilterOptionList will return list of active filters`() {
        val filterList = dynamicFilterModel.data.filter
        val nonFilterPair = SearchApiConst.Q to "samsung"
        val filterOption = filterList.optionList().find { it.key == SearchApiConst.OFFICIAL }!!
        val filterPair = filterOption.key to filterOption.value
        val parameter = mapOf(nonFilterPair, filterPair)

        val filterState = FilterState().from(
            parameter,
            filterList,
        )

        assertThat(filterState.activeFilterOptionList, `is`(listOf(filterOption)))
    }

    @Test
    fun `activeFilterMap is filterViewState as key value pairs`() {
        val filterList = dynamicFilterModel.data.filter
        val nonFilterPair = SearchApiConst.Q to "samsung"
        val filterOption = filterList.optionList().find { it.key == SearchApiConst.OFFICIAL }!!
        val filterPair = filterOption.key to filterOption.value
        val parameter = mapOf(nonFilterPair, filterPair)

        val filterState = FilterState().from(
            parameter,
            filterList,
        )

        assertThat(filterState.activeFilterMap, `is`(mapOf(filterPair)))
    }

    @Test
    fun `filterState apply filter will add unique id to filterViewState`() {
        val filterList = dynamicFilterModel.data.filter
        val existingAppliedFilter = filterList.optionList().last()
        val optionToApply = filterList.optionList().first()

        val filterState = FilterState()
            .from(
                parameter = mapOf(existingAppliedFilter.key to existingAppliedFilter.value),
                filterList = filterList
            )
            .setFilter(optionToApply, true)

        assertThat(
            filterState.filterViewState,
            `is`(setOf(
                existingAppliedFilter.uniqueId,
                optionToApply.uniqueId
            ))
        )
    }

    @Test
    fun `filterState remove filter will remove unique id from filterViewState`() {
        val filterList = dynamicFilterModel.data.filter
        val optionToRemove = filterList.optionList().first()

        val filterState = FilterState()
            .from(
                parameter = mapOf(optionToRemove.key to optionToRemove.value),
                filterList = filterList
            )
            .setFilter(optionToRemove, false)

        assertThat(
            filterState.filterViewState,
            `is`(setOf())
        )
    }

    @Test
    fun `filterState apply filter can clean up existing filter with same key`() {
        val filterList = dynamicFilterModel.data.filter
        val existingAppliedFilter = dynamicFilterModel.getCategoryFilter().options.first()
        val optionToApply = dynamicFilterModel.getCategoryFilter().options.last()

        val filterState = FilterState()
            .from(
                parameter = mapOf(existingAppliedFilter.key to existingAppliedFilter.value),
                filterList = filterList
            )
            .setFilter(optionToApply, true, isCleanUpFilterWithSameKey = true)

        assertThat(
            filterState.filterViewState,
            `is`(setOf(optionToApply.uniqueId))
        )
    }

    @Test
    fun `filterState apply or remove multiple filters`() {
        val filterList = dynamicFilterModel.data.filter
        val goldMerchant = filterList.optionList().find { it.key == "goldmerchant" }!!
        val officialStore = filterList.optionList().find { it.key == "official" }!!
        val freeShipping = filterList.optionList().find { it.key == "free_shipping" }!!
        val currentParameter = mapOf(
            SearchApiConst.Q to "samsung",
            goldMerchant.key to goldMerchant.value,
        )
        val currentState = FilterState()
            .from(filterList = filterList, parameter = currentParameter)
        val optionToSet = listOf(
            goldMerchant.clone().apply { inputState = false.toString() }, // un-apply
            officialStore.clone().apply { inputState = true.toString() }, // apply
            freeShipping.clone().apply { inputState = true.toString() }, // apply
        )

        val filterState = currentState.setFilter(optionToSet)

        assertThat(
            filterState.filterViewState,
            `is`(setOf(
                officialStore.uniqueId,
                freeShipping.uniqueId,
            ))
        )
    }

    @Test
    fun `filterState check is filter applied with Option`() {
        val filterList = dynamicFilterModel.data.filter
        val option = filterList.optionList()[0]

        val filterState = FilterState().from(
            parameter = mapOf(option.key to option.value),
            filterList = filterList,
        )

        assertTrue(filterState.isFilterApplied(option))
        assertTrue(filterState.isFilterApplied(option.uniqueId))
        assertFalse(filterState.isFilterApplied(filterList.optionList()[1]))
        assertFalse(filterState.isFilterApplied(filterList.optionList()[1].uniqueId))
    }

    @Test
    fun `parameter is key value pairs of given parameter and filters`() {
        val filterList = dynamicFilterModel.data.filter
        val optionToApply = filterList.optionList()[0]
        val currentParameter = mapOf(SearchApiConst.Q to "samsung")
        val currentState = FilterState().from(
            parameter = currentParameter,
            filterList = filterList,
        )

        val filterState = currentState.setFilter(optionToApply, true)

        assertThat(
            filterState.parameter,
            `is`(currentParameter + mapOf(optionToApply.key to optionToApply.value))
        )
    }

    @Test
    fun `appendFilterList will update filterViewState based on new filters`() {
        val filterList = dynamicFilterModel.data.filter
        val nonFilterPair = SearchApiConst.Q to "samsung"
        val filterOption = filterList.optionList().find { it.key == SearchApiConst.OFFICIAL }!!
        val filterPair = filterOption.key to filterOption.value
        val parameter = mapOf(nonFilterPair, filterPair)

        val currentState = FilterState().from(parameter)

        val filterState = currentState.appendFilterList(filterList)

        assertEquals(setOf(filterOption.uniqueId), filterState.filterViewState)
        assertTrue(filterState.isFilterActive)
    }
}
