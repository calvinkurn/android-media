package com.tokopedia.filter.common.data

import com.tokopedia.filter.testutils.jsonToObject
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class DynamicFilterModelTest {

    private val dynamicFilterModel =
        "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()

    @Test
    fun `get applied sort using parameter will return Sort object with same key and value`() {
        val appliedSort = dynamicFilterModel.data.sort[1]
        val mapParameter = mapOf<String, String>(appliedSort.key to appliedSort.value)

        val sort = dynamicFilterModel.getAppliedSort(mapParameter)

        assertThat(sort, `is`(notNullValue()))
        assertThat(sort!!.key, `is`(appliedSort.key))
        assertThat(sort!!.value, `is`(appliedSort.value))
        assertThat(sort!!.name, `is`(appliedSort.name))
    }

    @Test
    fun `get applied sort returns null if map parameter does not contain sort`() {
        val mapParameter = mapOf<String, String>()

        val sort = dynamicFilterModel.getAppliedSort(mapParameter)

        assertThat(sort, `is`(nullValue()))
    }

    @Test
    fun `get applied sort returns null if map parameter contains default sort`() {
        val sortKey = dynamicFilterModel.getSortKey()
        val sortValue = dynamicFilterModel.defaultSortValue
        val mapParameter = mapOf<String, String>(sortKey to sortValue)

        val sort = dynamicFilterModel.getAppliedSort(mapParameter)

        assertThat(sort, `is`(nullValue()))
    }
}