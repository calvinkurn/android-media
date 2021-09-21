package com.tokopedia.filter.newdynamicfilter.helper

import com.tokopedia.filter.common.data.Option
import org.junit.Assert.assertThat
import org.junit.Test
import org.hamcrest.CoreMatchers.`is` as shouldBe

class FilterHelperTest {

    @Test
    fun `test createParamsWithoutExcludes will exclude prefix from params`() {
        val inputMapParameter = mapOf(OptionHelper.EXCLUDE_PREFIX + Option.KEY_CATEGORY to "1234")

        val actualMapParameter = FilterHelper.createParamsWithoutExcludes(inputMapParameter)

        val expectedMapParameter = mapOf(Option.KEY_CATEGORY to "1234")
        assertMap(actualMapParameter, expectedMapParameter)
    }

    private fun assertMap(actualMapParameter: Map<String, String>, expectedMapParameter: Map<String, String>) {
        assertThat(actualMapParameter.size, shouldBe(expectedMapParameter.size))

        expectedMapParameter.forEach { (key, value) ->
            assertThat(actualMapParameter[key].toString(), shouldBe(value))
        }
    }

    @Test
    fun `test createParamsWithoutExcludes will remove exclude if key already exists`() {
        val inputMapParameter = mapOf(
                Option.KEY_CATEGORY to "3332",
                OptionHelper.EXCLUDE_PREFIX + Option.KEY_CATEGORY to "1234",
        )

        val actualMapParameter = FilterHelper.createParamsWithoutExcludes(inputMapParameter)

        val expectedMapParameter = mapOf(Option.KEY_CATEGORY to "3332")
        assertMap(actualMapParameter, expectedMapParameter)
    }
}